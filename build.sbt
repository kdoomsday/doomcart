name := """doomcart"""

version := "0.1-SNAPSHOT"

scalaVersion in ThisBuild := "2.11.5"

libraryDependencies ++= Common.dbDependencies

fork in Test := false

lazy val core = project in file("modules/core")

lazy val root = (project in file(".")).enablePlugins(PlayScala)
                      .dependsOn(simpleView).aggregate(simpleView)
                      .dependsOn(login).aggregate(login)

lazy val login = (project in file("modules/login")).enablePlugins(PlayScala)
                      .dependsOn(core)
                      .aggregate(core)

lazy val simpleView = (project in file("modules/simpleView"))
                          .enablePlugins(PlayScala)
                          .dependsOn(core)

//routesGenerator := InjectedRoutesGenerator