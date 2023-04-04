package apigateway

import apigateway.integration.http.IntegrationOneService
import io.grpc.ManagedChannelBuilder
import scalapb.zio_grpc.ZManagedChannel
import sttp.apispec.openapi.circe.yaml.RichOpenAPI
import sttp.apispec.openapi.{Info, OpenAPI}
import sttp.client3.httpclient.zio.HttpClientZioBackend
import sttp.tapir.docs.openapi.OpenAPIDocsInterpreter
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.server.ziohttp.ZioHttpInterpreter
import sttp.tapir.swagger.{SwaggerUI, SwaggerUIOptions}
import two_service.grpctwoservice.ZioGrpctwoservice.TwoServiceClient
import zhttp.service.Server
import zio.magic.ZioProvideMagicOps
import zio.{ExitCode, Task, URIO}

object ApiGatewayServerStart extends zio.App {

  val openApi: OpenAPI =
    OpenAPIDocsInterpreter()
      .toOpenAPI(
        ApiGatewayEndpoint.endpoints,
        Info(
          title = "Api gateway service",
          version = "1.0.0"
        )
      ).servers(List.empty)

  val openAPIEndpoints: Seq[ServerEndpoint[Any, Task]] =
    SwaggerUI[Task](
      openApi.toYaml,
      SwaggerUIOptions.default
        .yamlName("openapi.yaml")
        .pathPrefix(List("openapi"))
    )

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = {
    val server = for {
      serviceRoutes <- ApiGatewayController.routes
      server <- Server.start(
        8085,
        ZioHttpInterpreter().toHttp(serviceRoutes ++ openAPIEndpoints)
      )
    } yield server

    val grpcClientLayer = TwoServiceClient.live(
      ZManagedChannel(
        ManagedChannelBuilder.forAddress("localhost", 8087).usePlaintext()
      )
    )

    server.inject(
      ApiGatewayController.live,
      IntegrationOneService.live,
      HttpClientZioBackend().toLayer,
      grpcClientLayer
    )
      .exitCode
  }
}
