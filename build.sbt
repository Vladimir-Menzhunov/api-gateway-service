ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"

lazy val root = (project in file("."))
  .settings(
    name := "api-gateway-service-ex"
  )

Compile / PB.targets := Seq(
  scalapb.gen(grpc = true) -> (Compile / sourceManaged).value / "scalapb",
  scalapb.zio_grpc.ZioCodeGenerator -> (Compile / sourceManaged).value / "scalapb"
)

scalacOptions := Seq("-Ymacro-annotations")

libraryDependencies ++= List(
  Dependencies.zio,
  Dependencies.tapir,
  Dependencies.sttp,
  Dependencies.grpc,
).flatten