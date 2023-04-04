package twoservicegrpc

import io.grpc.Status
import two_service.grpctwoservice.{TwoRequest, TwoResponse}
import two_service.grpctwoservice.ZioGrpctwoservice.ZTwoService
import zio.{UIO, ZEnv, ZIO}

object GrpcTwoServiceImpl extends ZTwoService[ZEnv, Any] {
  override def runTwoMethod(request: TwoRequest): UIO[TwoResponse] =
    ZIO.succeed(TwoResponse(request.str + " grpc integration"))
}
