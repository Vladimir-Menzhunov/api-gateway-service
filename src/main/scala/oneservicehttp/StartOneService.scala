package oneservicehttp

import sttp.tapir.server.ziohttp.ZioHttpInterpreter
import zhttp.service.Server
import zio.{ExitCode, URIO}

object StartOneService extends zio.App {

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = {
    val server = {
      for {
        serviceRoutes <- OneController.routes
        server <- Server.start(
          8086,
          ZioHttpInterpreter().toHttp(serviceRoutes)
        )
      } yield ()
    }

    server.provideCustomLayer(OneController.live).exitCode
  }
}
