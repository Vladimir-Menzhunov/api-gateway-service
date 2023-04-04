import sbt._

object V {
  val zio = "1.0.14"
  val zioMagic = "0.3.12"
  val tapir = "1.2.3"
  val sttp = "3.8.7"
  val grpc = "1.39.0"
}

object Dependencies {
  val zio: List[ModuleID] = List(
    "dev.zio" %% "zio" % V.zio,
    "io.github.kitlangton" %% "zio-magic" % V.zioMagic
  )

  val tapir: List[ModuleID] = List(
    "com.softwaremill.sttp.tapir" %% "tapir-openapi-docs" % V.tapir,
    "com.softwaremill.sttp.tapir" %% "tapir-json-circe" % V.tapir,
    "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-bundle" % V.tapir,
    "com.softwaremill.sttp.tapir" %% "tapir-zio1-http-server" % V.tapir
  )

  val sttp: List[ModuleID] = List(
    "com.softwaremill.sttp.client3" %% "zio1" % V.sttp,
    "com.softwaremill.sttp.client3" %% "circe" % V.sttp
  )

  val grpc: List[ModuleID] = List(
    "io.grpc" % "grpc-netty" % V.grpc,
    "com.thesamet.scalapb" %% "scalapb-runtime-grpc" % scalapb.compiler.Version.scalapbVersion,
  )
}
