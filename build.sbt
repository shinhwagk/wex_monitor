import sbt.Keys._

val AkkaVersion = "2.4.8"
val PlayVersion = "2.5.0"
val SlickVersion = "3.1.1"
val HikariVersion = "2.4.7"
val ngVersion = "2.0.0-rc.4"

lazy val commonSettings = Seq(
  organization := "com.wex",
  version := "0.1.0",
  scalaVersion := "2.11.8",
  libraryDependencies ++= Seq(
    "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
    "com.typesafe.akka" %% "akka-http-core" % AkkaVersion,
    "com.typesafe.akka" %% "akka-http-experimental" % AkkaVersion,
    "com.typesafe.akka" %% "akka-http-spray-json-experimental" % AkkaVersion,
    "com.typesafe.play" %% "play-ws" % PlayVersion,
    "mysql" % "mysql-connector-java" % "5.1.38",
    "com.typesafe.slick" %% "slick-hikaricp" % SlickVersion,
    "com.zaxxer" % "HikariCP" % HikariVersion,
    "org.slf4j" % "slf4j-nop" % "1.6.4",
    "org.webjars.npm" % "angular__common" % ngVersion,
    "org.webjars.npm" % "angular__core" % ngVersion,
    "org.webjars.npm" % "angular__http" % ngVersion,
    "org.webjars.npm" % "angular__platform-browser" % ngVersion,
    "org.webjars.npm" % "angular__platform-browser-dynamic" % ngVersion,
    "org.webjars.npm" % "angular__upgrade" % ngVersion,
    "org.webjars.npm" % "angular__router" % "3.0.0-beta.2",
    "org.webjars.npm" % "angular__forms" % "0.2.0",
    "org.webjars.npm" % "angular2-in-memory-web-api" % "0.0.14",
    "org.webjars.npm" % "systemjs" % "0.19.31",
    "org.webjars.npm" % "core-js" % "2.4.0",
    "org.webjars.npm" % "reflect-metadata" % "0.1.3",
    "org.webjars.npm" % "rxjs" % "5.0.0-beta.10",
    "org.webjars.npm" % "zone.js" % "0.6.12",
    "org.webjars.npm" % "bootstrap" % "4.0.0-alpha.2",
    "org.webjars.npm" % "jquery" % "2.2.3",
    "org.webjars.npm" % "tether" % "1.3.2"
//    "com.typesafe.play" %% "play-slick" % "2.0.0",
//    "com.typesafe.play" %% "play-slick-evolutions" % "2.0.0",
//    jdbc
  )
)

lazy val master = (project in file("master")).
  settings(commonSettings: _*).
  settings(
    name := "master"
  ).enablePlugins(JavaAppPackaging)

lazy val agent = (project in file("agent")).
  settings(commonSettings: _*).
  settings(
    name := "agent"
  ).enablePlugins(JavaAppPackaging)

lazy val manage = (project in file("manage")).
  settings(commonSettings: _*).
  settings(
  name := "manage"
).enablePlugins(PlayScala)