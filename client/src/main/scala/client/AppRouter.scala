package template.client

import template.client.serverevents.ServerEventsList
import template.client.serverlogs.ServerLog
import template.client.foos.FoosList
import japgolly.scalajs.react.extra.router._
import japgolly.scalajs.react.vdom.prefix_<^._

sealed trait Page
object Page {
  case object Home extends Page
  case object ServerEvents extends Page
  case object ServerLogs extends Page
  case object Foos extends Page
}

object AppRouter {
  def apply() = Router(BaseUrl.fromWindowOrigin, routerConfig)()

  def routerConfig = RouterConfigDsl[Page].buildConfig { dsl =>
    import Page._
    import dsl._

    (emptyRule
      | staticRoute(root, Home) ~> render(<.h1("Home"))
      | staticRoute("/#server-events", ServerEvents) ~> render(ServerEventsList.component())
      | staticRoute("/#server-logs", ServerLogs) ~> render(ServerLog.component())
      | staticRoute("/#foos", Foos) ~> render(FoosList.component())
    )
      .notFound(redirectToPage(Home)(Redirect.Replace))
      .renderWith(Layout)
  }
}
