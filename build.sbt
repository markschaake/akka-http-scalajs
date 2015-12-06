val akkaVersion = "2.4.1"

def akkaModule(id: String) = "com.typesafe.akka" %% s"akka-$id" % akkaVersion

name := "akka-http-websockets"

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  akkaModule("actor"),
  akkaModule("testkit") % "test",
  "io.spray" %%  "spray-json" % "1.3.2",
  "com.typesafe.akka" % "akka-http-experimental_2.11" % "2.0-M2",
  "com.typesafe.akka" % "akka-http-spray-json-experimental_2.11" % "2.0-M2"
)
