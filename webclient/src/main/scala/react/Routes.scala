package example.akkwebsockets.webclient

import example.akkwebsockets.webclient.todos.TodoList
import example.akkwebsockets.webclient.serverevents.ServerEventsList
import japgolly.scalajs.react.extra.router._
import japgolly.scalajs.react.vdom.prefix_<^._
import scalacss.ScalaCssReact._

sealed trait Page

object Page {
  case object Home extends Page
  case object ServerEvents extends Page
  case object Todos extends Page
}

import Page._

object Routes {
  val routerConfig = RouterConfigDsl[Page].buildConfig { dsl =>
    import dsl._

    (emptyRule
      | staticRoute(root, Home) ~> render(<.h1("Home"))
      | staticRoute("/#server-events", ServerEvents) ~> render(ServerEventsList.component())
      | staticRoute("/#todos", Todos) ~> render(TodoList.component())
    )
      .notFound(redirectToPage(Home)(Redirect.Replace))
      .renderWith(layout)
  }

  def navItem(ctl: RouterCtl[Page], r: Resolution[Page], page: Page, text: String) =
    <.li(
      ctl.link(page)(text),
      ^.classSet(
        "active" -> (page == r.page)
      )
    )

  def layout(ctl: RouterCtl[Page], r: Resolution[Page]) =
    <.div(Styles.app,
      <.header(Styles.header, <.h1("This is a header")),
      <.div(Styles.main,
        <.nav(Styles.nav,
          <.ul(
            navItem(ctl, r, Page.Todos, "To Dos"),
            navItem(ctl, r, Page.ServerEvents, "Server Events")
          )
        ),
        <.div(Styles.content, r.render())
      ),
      <.footer(Styles.footer, "Footer")
    )
}
