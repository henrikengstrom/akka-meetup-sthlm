import sbt._
import sbt.Keys._

object AkkaDemoBuild extends Build {
  val Organization = "akkademo"
  val Version      = "1.0-SNAPSHOT"
  val ScalaVersion = "2.9.2"

  lazy val akkademo = Project(
    id = "akkademo",
    base = file("."),
    aggregate = Seq(common, processor, service)
  )

  lazy val common = Project(
    id = "common",
    base = file("common"),
    settings = defaultSettings ++ Seq(libraryDependencies ++= Dependencies.akkademo)
  )

  lazy val processor = Project(
    id = "processor",
    base = file("processor"),
    dependencies = Seq(common),
    settings = defaultSettings ++ Seq(
      libraryDependencies ++= Dependencies.akkademo
    )
  )

  lazy val service = Project(
    id = "service",
    base = file("service"),
    dependencies = Seq(common),
    settings = defaultSettings ++ Seq(
      libraryDependencies ++= Dependencies.akkademo
    )
  )

  lazy val defaultSettings = Defaults.defaultSettings ++ Seq(
    resolvers += "Typesafe Repo" at "http://repo.typesafe.com/typesafe/releases/",

    // compile options
    scalacOptions ++= Seq("-encoding", "UTF-8", "-optimise", "-deprecation", "-unchecked"),
    javacOptions  ++= Seq("-Xlint:unchecked", "-Xlint:deprecation"),

    // disable parallel tests
    parallelExecution in Test := false
  )  
}

object Dependencies {
  import Dependency._
  val akkademo = Seq(akkaActor, scalaTest, jUnit)
}

object Dependency {
  object Version {
    val Akka      = "2.0.3"
    val Scalatest = "1.6.1"
    val JUnit     = "4.5"
  }

  // ---- Application dependencies ----

  val akkaActor     = "com.typesafe.akka"         % "akka-actor"          % Version.Akka

  // ---- Test dependencies ----

  val scalaTest   = "org.scalatest"       % "scalatest_2.9.0"          % Version.Scalatest  % "test"
  val jUnit       = "junit"               % "junit"                    % Version.JUnit      % "test"
}
