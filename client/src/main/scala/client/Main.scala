package template.client

import template.client.serverevents.ServerEventListStyle
import template.client.todos.TodoListStyle
import org.scalajs.dom.window

import scala.scalajs.js.JSApp
import scalacss.Defaults._
import scalacss.ScalaCssReact._

object Main extends JSApp {
  def main(): Unit = {
    LayoutStyle.addToDocument()
    TodoListStyle.addToDocument()
    ServerEventListStyle.addToDocument()
    AppRouter() render window.document.getElementById("app")
  }
}
