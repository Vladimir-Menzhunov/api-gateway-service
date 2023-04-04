package apigateway

import apigateway.integration.http.{
  IntegrationOneService,
  IntegrationOneServiceImpl
}
import common.{
  RequestForOneService,
  RequestForTwoService,
  ResponseFromOneService,
  ResponseFromTwoService
}
import sttp.tapir.Endpoint
import sttp.tapir.ztapir.RichZEndpoint
import two_service.grpctwoservice.{TwoRequest, TwoResponse}
import two_service.grpctwoservice.ZioGrpctwoservice.TwoServiceClient
import zio.{Has, UIO, ULayer, ZIO, ZLayer}

object ApiGatewayController {
  trait Service {
    def callMethodOneService(
        requestForOneService: RequestForOneService
    ): UIO[ResponseFromOneService]

    def callMethodTwoService(
        requestForTwoService: RequestForTwoService
    ): UIO[ResponseFromTwoService]
  }

  private def register[Request, Response](
      endpoint: ApiEndpoint[Request, Response],
      handler: Request => UIO[Response]
  ) = {
    endpoint.zServerLogic(handler(_))
  }

  val routes = {
    for {
      controller <- ZIO.service[ApiGatewayController.Service]
    } yield List(
      register(
        ApiGatewayEndpoint.callMethodFirstServiceEndpoint,
        controller.callMethodOneService
      ),
      register(
        ApiGatewayEndpoint.callMethodSecondServiceEndpoint,
        controller.callMethodTwoService
      )
    )
  }

  val live = ZLayer
    .fromServices[
      IntegrationOneService.Service,
      TwoServiceClient.Service,
      ApiGatewayController.Service
    ]((integrationOneService, grpcClient) =>
      new ApiGatewayControllerImpl(integrationOneService, grpcClient)
    )
}

private class ApiGatewayControllerImpl(
    integrationOneService: IntegrationOneService.Service,
    grpcTwoServerClient: TwoServiceClient.Service
) extends ApiGatewayController.Service {
  override def callMethodOneService(
      requestForOneService: RequestForOneService
  ): UIO[ResponseFromOneService] =
    integrationOneService.oneMethod(requestForOneService).orDie

  override def callMethodTwoService(
      requestForTwoService: RequestForTwoService
  ): UIO[ResponseFromTwoService] =
    grpcTwoServerClient
      .runTwoMethod(TwoRequest(requestForTwoService.requestStr))
      .map(res => ResponseFromTwoService(res.str))
      .orDieWith(status => status.asException())
}
