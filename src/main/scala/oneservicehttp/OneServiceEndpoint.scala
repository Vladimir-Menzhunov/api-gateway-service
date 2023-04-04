package oneservicehttp

import common.JsonProtocol.codecRequestForOneService
import common.{RequestForOneService, ResponseFromOneService}
import io.circe.generic.codec.DerivedAsObjectCodec.deriveCodec
import sttp.tapir.generic.auto.schemaForCaseClass
import sttp.tapir.json.circe.jsonBody
import sttp.tapir.ztapir._
import sttp.tapir.{Endpoint, endpoint}

object OneServiceEndpoint {
  val methodEndpoint: Endpoint[Unit, RequestForOneService, Unit, ResponseFromOneService, Any] =
    endpoint.get
      .summary("Метод первого сервиса")
      .in("one" / "method")
      .in(query[RequestForOneService]("requestStr"))
      .out(jsonBody[ResponseFromOneService])

  val endpoints = List(methodEndpoint)
}
