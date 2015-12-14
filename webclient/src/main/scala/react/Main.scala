package example.akkwebsockets.webclient

import example.akkwebsockets.webclient.serverevents.ServerEventListStyle
import example.akkwebsockets.webclient.todos.TodoListStyle
import japgolly.scalajs.react.ReactDOM
import japgolly.scalajs.react.extra.router._
import org.scalajs.dom.window

import scala.scalajs.js.JSApp
import scalacss.Defaults._
import scalacss.ScalaCssReact._

object Main extends JSApp {
  def main(): Unit = {
    Styles.addToDocument()
    TodoListStyle.addToDocument()
    ServerEventListStyle.addToDocument()
    val router = Router(BaseUrl.fromWindowOrigin, Routes.routerConfig)
    router() render window.document.getElementById("app")
  }
}
