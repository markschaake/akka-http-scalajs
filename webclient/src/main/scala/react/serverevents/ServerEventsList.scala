package example.akkwebsockets.webclient.serverevents

import example.akkwebsockets.ServerEvent
import japgolly.scalajs.react.BackendScope
import japgolly.scalajs.react.Callback
import japgolly.scalajs.react.ReactComponentB
import japgolly.scalajs.react.vdom.prefix_<^._
import org.scalajs.dom.MessageEvent
import org.scalajs.dom.WebSocket
import org.scalajs.dom.window
import rx._
import upickle.default._

object ServerEventsStore {
  private val MaxEventsRetained = 20

  /** Server events buffer with most recent at the head */
  val events = Var[Vector[ServerEvent]](Vector.empty[ServerEvent])

  /** The most recent server event */
  val lastEvent = Var[Option[ServerEvent]](None)

  private val ws = {
    val loc = window.location
    val wsProtocol = if (loc.protocol == "http:") "ws:" else "wss:"
    val wsUrl = s"${wsProtocol}//${loc.host}/server-events"
    new WebSocket(url = wsUrl)
  }
  ws.onmessage = { evt: MessageEvent =>
    val serverEvent = read[ServerEvent](evt.data.asInstanceOf[String])
    events() = (serverEvent +: events()).take(MaxEventsRetained)
    lastEvent() = Some(serverEvent)
  }
}

class ServerEventsListBackend(scope: BackendScope[Unit, Vector[ServerEvent]]) {
  Obs(ServerEventsStore.events) {
    if (scope.isMounted) {
      scope.setState(ServerEventsStore.events()).runNow()
    }
  }
  def render(state: Vector[ServerEvent]) = {
    <.div(
      <.h2("Server Events"),
      <.ul(
        state.zipWithIndex map { case (e, i) =>
          <.li(^.key := i.toString, e.toString)
        }
      )
    )
  }
}


object ServerEventsList {
  val component = ReactComponentB[Unit]("ServerEventsList")
    .initialState(ServerEventsStore.events())
    .renderBackend[ServerEventsListBackend]
    .buildU
}
