package oneservicehttp

import common.{RequestForOneService, ResponseFromOneService}
import sttp.tapir.ztapir._
import zio.{Has, UIO, ULayer, ZIO, ZLayer}

object OneController {
  trait Service {
    def method(request: RequestForOneService): UIO[ResponseFromOneService]
  }

  val routes =
    for {
      service <- ZIO.service[OneController.Service]
    } yield List(
      OneServiceEndpoint.methodEndpoint.zServerLogic(service.method)
    )

  val live: ULayer[Has[OneController.Service]] = ZLayer.succeed(new OneControllerImpl())
}

class OneControllerImpl() extends OneController.Service {
  override def method(request: RequestForOneService): UIO[ResponseFromOneService] =
    ZIO.succeed(ResponseFromOneService(request.requestStr + " integration one service"))
}
