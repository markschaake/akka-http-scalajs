name := "akka-http-websockets"

version := "1.0"

scalaVersion := "2.11.7"

lazy val shared = (crossProject.crossType(CrossType.Pure) in file ("shared")).settings(
  libraryDependencies ++= Seq(
    "com.lihaoyi" %%% "upickle" % "0.3.6"
  )
)
lazy val sharedJvm = shared.jvm
lazy val sharedJs = shared.js
lazy val client = project.dependsOn(sharedJs)

lazy val jsResources = taskKey[Seq[File]](
  "All scalajs generated JS files, including source maps"
)

jsResources := {
  // this sets up a dependency on fastOptJS. For production, we'd want to run
  // fullOptJs instead
  val fastOpt = (fastOptJS in (client, Compile)).value.data
  val dir = (crossTarget in (client, Compile)).value
  dir.listFiles.filter(f => f.getName.endsWith(".js") || f.getName.endsWith(".js.map"))
}

lazy val server = project.dependsOn(sharedJvm).settings(
  (resources in Compile) := {
    (resources in Compile).value ++ (jsResources in LocalRootProject).value
  }
)

// TODO add UniversalPackager plugin and set up fullOptJs as a dependency for
// staging the server application.
