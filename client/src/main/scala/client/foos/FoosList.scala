package template.client.foos

import japgolly.scalajs.react.BackendScope
import japgolly.scalajs.react.ReactComponentB
import japgolly.scalajs.react.vdom.prefix_<^._
import template.client.serverevents.ServerEventsStore

import template.models.ServerEvent
import template.models.ServerEvent._

import rx._

object FoosStore {

  val foos = Var(Vector.empty[String])

  Obs(ServerEventsStore.lastEvent) {
    ServerEventsStore.lastEvent() match {
      case Some(FooUpdated(foo)) if !foos().contains(foo) => foos() = foo +: foos()
      case Some(FooDeleted(foo)) => foos() = foos().filterNot(_ == foo)
      case _ => // do nothing
    }
  }
}

class FoosListBackend(scope: BackendScope[Unit, Vector[String]]) {
  Obs(FoosStore.foos) {
    if (scope.isMounted) {
      scope.setState(FoosStore.foos()).runNow()
    }
  }

  def render(state: Vector[String]) = {
    <.div(
      <.h1("Foos"),
      <.ul(
        state.zipWithIndex map {
          case (foo, i) => <.li(^.key := i.toString, foo)
        }
      )
    )
  }
}

object FoosList {
  val component = ReactComponentB[Unit]("FoosList")
    .initialState(FoosStore.foos())
    .renderBackend[FoosListBackend]
    .buildU
}
