enablePlugins(ScalaJSPlugin)

scalaJSStage in Global := FastOptStage

// This uses local NodeJS install instead of Rhino for compilation.
// This speeds up the client build, but sacrifices build portability.
// For a real project, this should be a local setting that is not checked
// into source control. I have this locally in client/local.sbt which is
// gitignored.
//postLinkJSEnv in Global := NodeJSEnv().value

libraryDependencies ++= Seq(
  "com.github.japgolly.scalacss" %%% "core" % "0.3.1",
  "com.github.japgolly.scalacss" %%% "ext-react" % "0.3.1",
  "com.github.japgolly.scalajs-react" %%% "core" % "0.10.0",
  "com.github.japgolly.scalajs-react" %%% "extra" % "0.10.0",
  "com.lihaoyi" %%% "scalarx" % "0.2.8",
  "com.lihaoyi" %%% "upickle" % "0.3.6",
  "org.scala-js" %%% "scalajs-dom" % "0.8.0"
)

jsDependencies ++= Seq(
  "org.webjars.bower" % "react" % "0.14.3"
    /        "react-with-addons.js"
    minified "react-with-addons.min.js"
    commonJSName "React",

  "org.webjars.bower" % "react" % "0.14.3"
    /         "react-dom.js"
    minified  "react-dom.min.js"
    dependsOn "react-with-addons.js"
    commonJSName "ReactDOM"
)
