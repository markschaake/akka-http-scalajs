package template.client

import template.client.serverevents.ServerEventListStyle
import template.client.serverlogs.ServerLogStyle
import org.scalajs.dom.window

import scala.scalajs.js.JSApp
import scalacss.Defaults._
import scalacss.ScalaCssReact._

object Main extends JSApp {
  def main(): Unit = {
    LayoutStyle.addToDocument()
    ServerEventListStyle.addToDocument()
    ServerLogStyle.addToDocument()
    AppRouter() render window.document.getElementById("app")
  }
}
