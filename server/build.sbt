val akkaVersion = "2.4.1"

def akkaModule(id: String) = "com.typesafe.akka" %% s"akka-$id" % akkaVersion

libraryDependencies ++= Seq(
  akkaModule("actor"),
  akkaModule("testkit") % "test",
  "com.typesafe.akka" % "akka-http-experimental_2.11" % "2.0-M2"
)

mainClass in reStart := Some("template.server.Main")
