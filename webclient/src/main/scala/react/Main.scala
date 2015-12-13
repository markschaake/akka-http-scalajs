package example.akkwebsockets.webclient

import example.akkwebsockets.webclient.todos.TodoListStyle

import japgolly.scalajs.react.ReactDOM
import org.scalajs.dom.window
import scalacss.Defaults._
import scalacss.ScalaCssReact._

import scala.scalajs.js.JSApp

object Main extends JSApp {
  def main(): Unit = {
    Styles.addToDocument()
    TodoListStyle.addToDocument()
    ReactDOM.render(App.component(), window.document.getElementById("app"))
    println("Hello world!")
  }
}
