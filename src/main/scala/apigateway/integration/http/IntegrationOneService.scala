package apigateway.integration.http

import apigateway.integration.http.IntegrationOneService.baseUri
import common.{RequestForOneService, ResponseFromOneService}
import io.circe.generic.codec.DerivedAsObjectCodec.deriveCodec
import sttp.client3.circe.asJson
import sttp.client3.{UriContext, basicRequest}
import sttp.client3.httpclient.zio.SttpClient
import sttp.model.Uri
import zio.{Task, ZIO, ZLayer}

object IntegrationOneService {
  trait Service {
    def oneMethod(request: RequestForOneService): Task[ResponseFromOneService]
  }

  val baseUri: Uri = uri"http://localhost:8086"

  val live = ZLayer.fromService[SttpClient.Service, IntegrationOneService.Service](
    sttpClientService => new IntegrationOneServiceImpl(sttpClientService)
  )
}

class IntegrationOneServiceImpl(httpClient: SttpClient.Service) extends IntegrationOneService.Service {
  override def oneMethod(requestOne: RequestForOneService): Task[ResponseFromOneService] = {
    val url = baseUri
      .addPath(Seq("one", "method"))
      .withParam("requestStr", requestOne.requestStr)

    val request =
      basicRequest
        .get(url)
        .response(asJson[ResponseFromOneService])

    httpClient.send(request).flatMap(response => ZIO.fromEither(response.body))
  }
}
