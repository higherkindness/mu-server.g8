package com.example.pingpong.server

import cats.effect._
import cats.syntax.functor._
import com.example.pingpong.protocol._
import com.example.pingpong.protocol.PingPongService
import io.chrisdavenport.log4cats.Logger

class PingPongServiceHandler[F[_]: Async: Logger: Timer] extends PingPongService[F] {
  val serviceName = "PingPongService"

  override def pingPong(request: Ping): F[Pong] =
    Logger[F].info(s"\$serviceName Request: \$request").as(Pong("Pong!"))

}
