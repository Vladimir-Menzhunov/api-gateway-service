package apigateway

import common.JsonProtocol.codecRequestForOneService
import common.{RequestForOneService, RequestForTwoService, ResponseFromOneService, ResponseFromTwoService}
import io.circe.generic.codec.DerivedAsObjectCodec.deriveCodec
import sttp.tapir.generic.auto.schemaForCaseClass
import sttp.tapir.json.circe.jsonBody
import sttp.tapir.{Endpoint, endpoint}
import sttp.tapir.ztapir._

object ApiGatewayEndpoint {
  val callMethodFirstServiceEndpoint: Endpoint[Unit, RequestForOneService, Unit, ResponseFromOneService, Any] =
    endpoint.get
      .summary("Вызов метода из первого сервиса")
      .in("api" / "call-one")
      .in(query[RequestForOneService]("requestStr"))
      .out(jsonBody[ResponseFromOneService])

  val callMethodSecondServiceEndpoint: Endpoint[Unit, RequestForTwoService, Unit, ResponseFromTwoService, Any] =
    endpoint.get
      .summary("Вызов метода из второго сервиса")
      .in("api" / "call-two")
      .in(RequestForTwoService.endpointInput)
      .out(jsonBody[ResponseFromTwoService])

  val endpoints = List(callMethodFirstServiceEndpoint, callMethodSecondServiceEndpoint)
}
