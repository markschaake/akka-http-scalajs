package example.akkwebsockets.webclient.serverevents

import example.akkwebsockets.webclient.BaseStyle

import example.akkwebsockets.ServerEvent
import example.akkwebsockets.ServerEvent._
import japgolly.scalajs.react.BackendScope
import japgolly.scalajs.react.Callback
import japgolly.scalajs.react.ReactComponentB
import japgolly.scalajs.react.vdom.prefix_<^._
import org.scalajs.dom.MessageEvent
import org.scalajs.dom.WebSocket
import org.scalajs.dom.window
import rx._
import upickle.default._

import scalacss.ScalaCssReact._
import scalacss.Defaults._

object ServerEventListStyle extends BaseStyle {
  import dsl._

  val serverEventList = style(
    addClassName("server-event-list")
  )

  val serverStatusUpdate = style(
    backgroundColor.yellow
  )
}

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
  import ServerEventsList._

  Obs(ServerEventsStore.events) {
    if (scope.isMounted) {
      scope.setState(ServerEventsStore.events()).runNow()
    }
  }
  def render(state: Vector[ServerEvent]) = {
    <.div(ServerEventListStyle.serverEventList,
      <.h2("Server Events"),
      <.ul(
        state.zipWithIndex map {
          case (e: ServerStatusUpdate, i) => ServerStatusUpdateItem.withKey(i.toString)(e)
          case (e, i) => ServerEventItem.withKey(i.toString)(e)
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

  val ServerStatusUpdateItem = ReactComponentB[ServerStatusUpdate]("ServerStatusUpdate")
    .render(update =>
      <.li(ServerEventListStyle.serverStatusUpdate, ^.cls := "server-status-update", update.props.toString)
    )
    .build

  val ServerEventItem = ReactComponentB[ServerEvent]("ServerEvent")
    .render(evt => <.li(evt.props.toString))
    .build
}
