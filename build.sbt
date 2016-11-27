name := """doomcart"""

version := "0.1-SNAPSHOT"

scalaVersion in ThisBuild := "2.11.8"

libraryDependencies ++= Seq(
  "com.github.nscala-time" %% "nscala-time" % "2.14.0",
  "org.scalacheck" %% "scalacheck" % "1.13.2" % "test",

  "com.typesafe.play" %% "play-slick" % "2.0.0",
  "com.typesafe.play" %% "play-slick-evolutions" % "2.0.0",
  "postgresql" % "postgresql" % "9.1-901.jdbc4",

  "org.webjars" %% "webjars-play" % "2.3.0-2",
  "be.objectify" %% "deadbolt-scala" % "2.5.0",
  "org.webjars.npm" % "materialize-css" % "0.97.3",
  "org.webjars.npm" % "alertifyjs" % "1.4.1"
)

lazy val root = (project in file(".")).enablePlugins(PlayScala)
