package twoservicegrpc

import io.grpc.ServerBuilder
import io.grpc.protobuf.services.ProtoReflectionService
import scalapb.zio_grpc.{ServerLayer, ServiceList}
import zio.{ExitCode, URIO}

object GrpcTwoService extends zio.App {
  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = {

    val server =
      for {
        _ <- ServerLayer
          .fromServiceList(
            ServerBuilder
              .forPort(8087)
              .addService(ProtoReflectionService.newInstance()),
            ServiceList.add(GrpcTwoServiceImpl))
          .build
          .useForever
      } yield ()


    server.exitCode
  }
}
