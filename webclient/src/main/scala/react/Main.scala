package example.akkwebsockets.webclient

import example.akkwebsockets.webclient.serverevents.ServerEventListStyle
import example.akkwebsockets.webclient.todos.TodoListStyle
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
