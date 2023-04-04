package common

import io.circe.generic.JsonCodec
import sttp.tapir.EndpointIO.annotations.{description, query}
import sttp.tapir.EndpointInput
import sttp.tapir.EndpointInput.derived

@JsonCodec
case class RequestForOneService(
    @query
    @description("Строка запроса")
    requestStr: String
)

case class ResponseFromOneService(
    responseStr: String
)

@JsonCodec
case class RequestForTwoService(
    @query
    @description("Строка запроса")
    requestStr: String
)

object RequestForTwoService {
  val endpointInput: EndpointInput[RequestForTwoService] = derived[RequestForTwoService]
}

case class ResponseFromTwoService(
    responseStr: String
)
