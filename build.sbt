name := """doomcart"""

version := "0.1-SNAPSHOT"

scalaVersion in ThisBuild := "2.11.5"

libraryDependencies ++= Seq(
  "org.webjars" %% "webjars-play" % "2.3.0-2",
  "com.typesafe.play" %% "play-slick" % "0.8.1"
)

fork in Test := false

lazy val core = project in file("modules/core")

lazy val root = (project in file(".")).enablePlugins(PlayScala)
                    .dependsOn(simpleView)
                    .aggregate(simpleView)

lazy val simpleView = (project in file("modules/simpleView"))
                          .enablePlugins(PlayScala)
                          .dependsOn(core)

//routesGenerator := InjectedRoutesGenerator