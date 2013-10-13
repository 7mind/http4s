import Dependencies._

name := "http4s-examples"

description := "Examples of using http4s on various backends"

libraryDependencies ++= Seq(
  Effectful,
  LogbackClassic,
  JettyServer,
  JettyServlet,
  ServletApi
)

fork in run := true