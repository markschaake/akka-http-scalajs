package example.akkwebsockets.webclient

import example.akkwebsockets.webclient.todos.TodoList
import example.akkwebsockets.webclient.serverevents.ServerEventsList
import japgolly.scalajs.react.extra.router._
import japgolly.scalajs.react.vdom.prefix_<^._

sealed trait Page

object Page {
  case object Home extends Page
  case object ServerEvents extends Page
  case object Todos extends Page
}

import Page._

object AppRouter {

  def apply() = Router(BaseUrl.fromWindowOrigin, routerConfig)()

  def routerConfig = RouterConfigDsl[Page].buildConfig { dsl =>
    import dsl._

    (emptyRule
      | staticRoute(root, Home) ~> render(<.h1("Home"))
      | staticRoute("/#server-events", ServerEvents) ~> render(ServerEventsList.component())
      | staticRoute("/#todos", Todos) ~> render(TodoList.component())
    )
      .notFound(redirectToPage(Home)(Redirect.Replace))
      .renderWith(Layout)
  }
}
