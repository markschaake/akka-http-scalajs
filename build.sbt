name := "akka-http-websockets"

version := "1.0"

scalaVersion := "2.11.7"

lazy val shared = (crossProject.crossType(CrossType.Pure) in file ("shared"))
lazy val sharedJvm = shared.jvm
lazy val sharedJs = shared.js
lazy val server = project.dependsOn(sharedJvm)
lazy val client = project.dependsOn(sharedJs)
