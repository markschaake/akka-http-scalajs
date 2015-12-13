import sbt._
import sbt.Keys._

object GlobalPlugin extends AutoPlugin {
  override def requires = plugins.JvmPlugin
  override def trigger = allRequirements

  override def projectSettings = Seq(
    scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature"),
    scalaVersion := "2.11.7"
  )
}
