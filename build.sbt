name := """doomcart"""

version := "0.1-SNAPSHOT"

scalaVersion in ThisBuild := "2.11.5"

libraryDependencies ++= Common.dbDependencies ++ Common.webDependencies

fork in Test := false

lazy val core = project in file("modules/core")

// Basis for all web modules
lazy val webCore = (project in file("modules/webCore"))
                      .enablePlugins(PlayScala)
                      .dependsOn(core).aggregate(core)

lazy val root = (project in file(".")).enablePlugins(PlayScala)
                      .dependsOn(simpleView).aggregate(simpleView)
                      .dependsOn(login).aggregate(login)

lazy val login = (project in file("modules/login"))
                      .enablePlugins(PlayScala)
                      .dependsOn(webCore)

lazy val simpleView = (project in file("modules/simpleView"))
                          .enablePlugins(PlayScala)
                          .dependsOn(webCore)

//routesGenerator := InjectedRoutesGenerator