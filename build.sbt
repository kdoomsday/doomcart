name := """doomcart"""

version := "0.1-SNAPSHOT"

scalaVersion in ThisBuild := "2.11.8"

libraryDependencies ++= Common.coreDependencies ++ Common.dbDependencies ++ Common.webDependencies

fork in Test := false

lazy val core = project in file("modules/core")

// Basis for all web modules
lazy val webCore = (project in file("modules/webCore"))
                      .enablePlugins(PlayScala)
                      .dependsOn(core)
                      .aggregate(core)

lazy val root = (project in file(".")).enablePlugins(PlayScala)
                      .dependsOn(webCore)
