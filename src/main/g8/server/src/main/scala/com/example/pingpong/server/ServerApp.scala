package com.example.pingpong.server

import cats.effect.{Effect, ExitCode, _}
import cats.syntax.either._
import cats.syntax.flatMap._
import cats.syntax.functor._
import com.example.pingpong.protocol.PingPongService
import higherkindness.mu.rpc.server.{AddService, GrpcServer}
import io.chrisdavenport.log4cats.Logger
import io.chrisdavenport.log4cats.slf4j.Slf4jLogger
import pureconfig.{ConfigReader, Derivation}
import pureconfig.generic.auto._

class ServerProgram[F[_]: ConcurrentEffect: Timer] {

  def runProgram(args: List[String]): F[ExitCode] =
    for {
      config  <- serviceConfig[PingPongServerConfig]
      logger  <- Slf4jLogger.fromName[F](config.name)
      exitCode <- serverProgram(config)(logger)
    } yield exitCode


  private def serviceConfig[Config](implicit reader: Derivation[ConfigReader[Config]]): F[Config] =
    Effect[F].fromEither(
      pureconfig
        .loadConfig[Config]
        .leftMap(e => new IllegalStateException(s"Error loading configuration: \$e"))
    )

  private def serverProgram(config: PingPongServerConfig)(implicit L: Logger[F]): F[ExitCode] = {

    implicit val PS: PingPongService[F] = new PingPongServiceHandler[F]

    for {
      shService <- PingPongService.bindService[F]
      server    <- GrpcServer.default[F](config.port, List(AddService(shService)))
      _         <- L.info(s"\${config.name} - Starting server at \${config.host}:\${config.port}")
      exitCode  <- GrpcServer.server(server).as(ExitCode.Success)
    } yield exitCode

  }
}

object ServerApp extends IOApp {
  def run(args: List[String]): IO[ExitCode] = new ServerProgram[IO].runProgram(args)
}
