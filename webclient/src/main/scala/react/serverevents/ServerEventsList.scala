package example.akkwebsockets.webclient.serverevents

import example.akkwebsockets.ServerEvent

import japgolly.scalajs.react.BackendScope
import japgolly.scalajs.react.Callback
import japgolly.scalajs.react.ReactComponentB
import japgolly.scalajs.react.vdom.prefix_<^._
import org.scalajs.dom.MessageEvent
import org.scalajs.dom.WebSocket
import org.scalajs.dom.window

import upickle.default._

class ServerEventsListBackend(scope: BackendScope[Unit, Vector[ServerEvent]]) {

  val loc = window.location
  val wsProtocol = if (loc.protocol == "http:") "ws:" else "wss:"
  val wsUrl = s"${wsProtocol}//${loc.host}/server-events"
  val ws = new WebSocket(url = wsUrl)
  ws.onmessage = { evt: MessageEvent =>
    val serverEvent = read[ServerEvent](evt.data.asInstanceOf[String])
    val cb = scope.modState (prev => (serverEvent +: prev).take(20))
    cb.runNow()
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
    .initialState(Vector.empty[ServerEvent])
    .renderBackend[ServerEventsListBackend]
    .buildU
}
