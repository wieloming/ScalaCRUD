import play.sbt.PlayImport._

name := """ScalaCRUD"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  specs2 % Test
)

libraryDependencies += "org.mockito" % "mockito-core" % "1.9.5" % "test"

libraryDependencies += "com.github.tototoshi" %% "play-joda-routes-binder" % "1.1.0"

routesImport += "com.github.tototoshi.play2.routes.JodaRoutes._"