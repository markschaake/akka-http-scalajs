package example.akkwebsockets.webclient

import example.akkwebsockets.webclient.todos.TodoList
import example.akkwebsockets.webclient.serverevents.ServerEventsList

import japgolly.scalajs.react.BackendScope
import japgolly.scalajs.react.ReactComponentB
import japgolly.scalajs.react.vdom.prefix_<^._
import scalacss.ScalaCssReact._

class AppBackend(scope: BackendScope[Unit, Unit]) {
  def render =
    <.div(Styles.app,
      <.header(Styles.header, <.h1("This is a header")),
      <.div(Styles.main,
        <.nav(Styles.nav,
          <.ul(
            <.li("To Dos"),
            <.li("Two")
          )
        ),
        <.div(Styles.content,
          ServerEventsList.component(),
          TodoList.component()
        )
      ),
      <.footer(Styles.footer, "Footer")
    )
}

object App {
  val component = ReactComponentB[Unit]("App")
    .renderBackend[AppBackend]
    .buildU
}
