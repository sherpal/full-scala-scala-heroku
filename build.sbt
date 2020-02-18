import sbt.Keys._
import sbtcrossproject.CrossPlugin.autoImport.{CrossType, crossProject}

name := "Full Stack Scala in Heroku"

version := "0.1"

scalaVersion := "2.12.10"

scalacOptions ++= Seq(
  "-deprecation",
  "-feature"
)

lazy val `shared` = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .disablePlugins(HerokuPlugin) // no need of Heroku for shared project
  .settings(
    SharedSettings()
  )
  .jvmSettings(
    SharedSettings.jvmSettings
  )

lazy val sharedJVM = `shared`.jvm
lazy val sharedJS = `shared`.js

/** Backend server uses Play framework */
lazy val `backend` = (project in file("./backend"))
  .enablePlugins(PlayScala)
  .settings(
    BackendSettings(),
    BackendSettings.herokuSettings(),
    libraryDependencies += guice // dependency injection
  )
  .dependsOn(sharedJVM)

/** Frontend will use react with Slinky */
lazy val `frontend` = (project in file("./frontend"))
  .disablePlugins(HerokuPlugin)
  .enablePlugins(ScalaJSBundlerPlugin)
  .settings(FrontendSettings())
  .dependsOn(sharedJS)

addCommandAlias("dev", ";frontend/fastOptJS::startWebpackDevServer;~frontend/fastOptJS")

addCommandAlias("build", "frontend/fullOptJS::webpack")

stage := {
  val webpackValue = (frontend / Compile / fullOptJS / webpack).value
  println(s"Webpack value is $webpackValue")
  (stage in backend).value
}

// sbt clean stage backend/deploy
