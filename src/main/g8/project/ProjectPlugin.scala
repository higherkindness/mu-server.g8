import higherkindness.mu.rpc.idlgen.IdlGenPlugin.autoImport._
import sbt.Keys._
import sbt._

object ProjectPlugin extends AutoPlugin {

  override def trigger: PluginTrigger = allRequirements

  object autoImport {

    lazy val V = new {
      val catsEffect     = "1.2.0"
      val log4cats       = "0.3.0"
      val logbackClassic = "1.2.3"
      val mu             = "0.18.0"
      val pureconfig     = "0.10.2"
      val circeVersion   = "0.11.1"
    }
  }

  import autoImport._

  private lazy val codeGenSettings: Seq[Def.Setting[_]] = Seq(
    idlType := "avro",
    srcGenSerializationType := "Avro",
    srcGenJarNames := Seq("ping-pong-protocol"),
    sourceGenerators in Compile += (srcGen in Compile).taskValue
  )

  private lazy val commonSettings: Seq[Def.Setting[_]] = Seq(
    libraryDependencies ++= Seq(
      "ch.qos.logback"    % "logback-classic" % V.logbackClassic,
      "io.chrisdavenport" %% "log4cats-core"  % V.log4cats,
      "io.chrisdavenport" %% "log4cats-slf4j" % V.log4cats
    )
  )

  lazy val noPublishSettings: Seq[Def.Setting[_]] = Seq(
    publish := ((): Unit),
    publishLocal := ((): Unit),
    publishArtifact := false
  )

  lazy val protocolSettings: Seq[Def.Setting[_]] = Seq(
    publishMavenStyle := true,
    crossPaths := false,
    libraryDependencies := Nil
  )

  lazy val serverSettings: Seq[Def.Setting[_]] = commonSettings ++
    codeGenSettings ++
    Seq(
      libraryDependencies ++= Seq(
        "io.higherkindness" %% "mu-rpc-server" % V.mu,
        "io.higherkindness" %% "mu-rpc-fs2"    % V.mu,
        "org.typelevel"         %% "cats-effect" % V.catsEffect,
        "com.github.pureconfig" %% "pureconfig"  % V.pureconfig
      ),
      libraryDependencies ++= Seq(
        "io.circe" %% "circe-core",
        "io.circe" %% "circe-generic",
        "io.circe" %% "circe-parser"
      ).map(_ % V.circeVersion)
    )


  override def projectSettings: Seq[Def.Setting[_]] =
    Seq(
      name := "ping-pong",
      organization := "com.example",
      scalaVersion := "2.12.8",
      scalacOptions := Seq(
        "-deprecation",
        "-encoding",
        "UTF-8",
        "-feature",
        "-language:existentials",
        "-language:higherKinds",
        "-language:implicitConversions",
        "-unchecked",
        "-Xlint",
        "-Yno-adapted-args",
        "-Ywarn-dead-code",
        "-Ywarn-numeric-widen",
        "-Ywarn-value-discard",
        "-Xfuture",
        "-Ywarn-unused-import"
      ),
      addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full)
    )
}
