import ProjectPlugin._

lazy val protocol = project
  .in(file("protocol"))
  .settings(moduleName := "ping-pong-protocol")
  .settings(protocolSettings)

lazy val server = project
  .in(file("server"))
  .settings(moduleName := "ping-pong-server")
  .settings(serverSettings)
  .dependsOn(protocol)

lazy val root = project
  .in(file("."))
  .settings(name := "ping-pong")
  .settings(noPublishSettings)
  .aggregate(protocol, server)
  .dependsOn(protocol, server)

addCommandAlias("runServer", "server/runMain com.example.pingpong.server.ServerApp")
