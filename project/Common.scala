import sbt._

object Common {
  val dbDependencies = Seq(
    "com.typesafe.play" %% "play-slick" % "2.0.0",
    "com.typesafe.play" %% "play-slick-evolutions" % "2.0.0",
    "postgresql" % "postgresql" % "9.1-901.jdbc4"
  )

  val webDependencies = Seq(
    "org.webjars" %% "webjars-play" % "2.3.0-2",
    "be.objectify" %% "deadbolt-scala" % "2.5.0"
  )
}