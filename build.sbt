import sbt.Keys._

val AkkaVersion = "2.4.8"
val PlayVersion = "2.5.0"

lazy val commonSettings = Seq(
  organization := "com.wex",
  version := "0.1.0",
  scalaVersion := "2.11.8",
  libraryDependencies ++= Seq(
    "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
    "com.typesafe.akka" %% "akka-http-core" % AkkaVersion,
    "com.typesafe.akka" %% "akka-http-experimental" % AkkaVersion,
    "com.typesafe.akka" %% "akka-http-spray-json-experimental" % AkkaVersion,
    "com.typesafe.play" %% "play-ws" % PlayVersion
  )
)

lazy val master = (project in file("master")).
  settings(commonSettings: _*).
  settings(
    name := "master"
  )

lazy val agent = (project in file("agent")).
  settings(commonSettings: _*).
  settings(
    name := "agent"
  )

lazy val manage = (project in file("manage")).
  settings(commonSettings: _*).
  settings(
    name := "manage"
  ).enablePlugins(PlayScala)