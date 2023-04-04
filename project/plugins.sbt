addSbtPlugin("com.thesamet" % "sbt-protoc" % "1.0.2")

libraryDependencies ++= Seq(
  "com.thesamet.scalapb.zio-grpc" %% "zio-grpc-codegen" % "0.5.0",
)