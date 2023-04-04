import sttp.tapir.Endpoint

package object apigateway {
  type ApiEndpoint[Request, Response] = Endpoint[Unit, Request, Unit, Response, Any]
}
