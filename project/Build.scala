import sbt._
import sbt.Keys._
import com.typesafe.startscript.StartScriptPlugin
import com.typesafe.sbtscalariform.ScalariformPlugin
import com.typesafe.sbtscalariform.ScalariformPlugin.ScalariformKeys

object AkkaDemoBuild extends Build {
  val Organization = "akkademo"
  val Version      = "1.0-SNAPSHOT"
  val ScalaVersion = "2.9.2"

  lazy val akkademo = Project(
    id = "akkademo",
    base = file("."),
    settings = defaultSettings ++
      Seq(StartScriptPlugin.stage in Compile := Unit),
    aggregate = Seq(common, processor, service, client)
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
    settings = defaultSettings ++
    StartScriptPlugin.startScriptForClassesSettings ++
    Seq(libraryDependencies ++= Dependencies.akkademo)
  )

  lazy val service = Project(
    id = "service",
    base = file("service"),
    dependencies = Seq(common),
    settings = defaultSettings ++
    StartScriptPlugin.startScriptForClassesSettings ++
    Seq(libraryDependencies ++= Dependencies.akkademo)
  )

  lazy val client = Project(
    id = "client",
    base = file("client"),
    dependencies = Seq(common, service),
    settings = defaultSettings ++
      StartScriptPlugin.startScriptForClassesSettings ++
      Seq(libraryDependencies ++= Dependencies.akkademo)
  )

  lazy val defaultSettings = Defaults.defaultSettings ++ formatSettings ++ Seq(
    resolvers += "Typesafe Repo" at "http://repo.typesafe.com/typesafe/releases/",

    // compile options
    scalacOptions ++= Seq("-encoding", "UTF-8", "-optimise", "-deprecation", "-unchecked"),
    javacOptions  ++= Seq("-Xlint:unchecked", "-Xlint:deprecation"),

    // disable parallel tests
    parallelExecution in Test := false
  )

  lazy val formatSettings = ScalariformPlugin.scalariformSettings ++ Seq(
    ScalariformKeys.preferences in Compile := formattingPreferences,
    ScalariformKeys.preferences in Test    := formattingPreferences
  )

  def formattingPreferences = {
    import scalariform.formatter.preferences._
    FormattingPreferences()
      .setPreference(RewriteArrowSymbols, true)
      .setPreference(AlignParameters, true)
      .setPreference(AlignSingleLineCaseStatements, true)
  }
}

object Dependencies {
  import Dependency._
  val akkademo = Seq(akkaActor, akkaRemote, scalaTest, jUnit)
}

object Dependency {
  object Version {
    val Akka      = "2.0.3"
    val Scalatest = "1.6.1"
    val JUnit     = "4.5"
  }

  // ---- Application dependencies ----

  val akkaActor   = "com.typesafe.akka"   % "akka-actor"              % Version.Akka
  val akkaRemote  = "com.typesafe.akka"   % "akka-remote"             % Version.Akka

  // ---- Test dependencies ----

  val scalaTest   = "org.scalatest"       % "scalatest_2.9.0"          % Version.Scalatest  % "test"
  val jUnit       = "junit"               % "junit"                    % Version.JUnit      % "test"
}
