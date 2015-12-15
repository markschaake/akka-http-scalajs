package template.client.serverevents

import japgolly.scalajs.react.BackendScope
import japgolly.scalajs.react.Callback
import japgolly.scalajs.react.ReactComponentB
import japgolly.scalajs.react.ReactElement
import japgolly.scalajs.react.vdom.prefix_<^._
import org.scalajs.dom.MessageEvent
import org.scalajs.dom.WebSocket
import org.scalajs.dom.window
import rx._
import template.client.StyleSheet
import template.models.ServerEvent
import template.models.ServerEvent._
import upickle.default._

import scalacss.Defaults._
import scalacss.ScalaCssReact._

object ServerEventListStyle extends StyleSheet {
  import dsl._

  val serverEventList = style(
    addClassName("server-event-list")
  )

  val eventMixin = mixin(
    marginTop(5.px),
    width(250.px),
    borderWidth(1.px),
    borderStyle(solid),
    borderRadius(2.px),
    unsafeChild("h3")(
      margin.`0`,
      padding(3.px),
      borderBottom(1.px, solid, colors.lightGray)
    ),
    unsafeChild(".details")(padding(5.px))
  )

  val serverStatusUpdate = style(
    eventMixin,
    borderColor(colors.darkGray),
    unsafeChild("div.stat")(
      unsafeChild(".term")(
        display.inlineBlock,
        fontWeight.bold,
        width(140.px)
      ),
      unsafeChild(".value")(display.inlineBlock)
    )
  )

  val fooUpdated = style(eventMixin, color.green, borderColor.green)

  val fooDeleted = style(eventMixin, color.red, borderColor.red)
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
      <.div(
        state.zipWithIndex map {
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

  val ServerEventItem = ReactComponentB[ServerEvent]("ServerEvent").render { callback =>
    def event(title: String, style: StyleA)(details: TagMod*) = <.div(style,
      <.h3(title),
      <.div(^.cls := "details", details)
    )
    def stat(name: String, value: String) =
      <.div(^.cls := "stat")(
        <.span(^.cls := "term", name),
        <.span(^.cls := "value", value)
      )
    callback.props match {
      case evt: ServerStatusUpdate => event("Server Status Update", ServerEventListStyle.serverStatusUpdate)(
        stat("Processors", evt.processors.toString),
        stat("JVM Free memory", s"${evt.freeMemory / 1000000} MB"),
        stat("JVM Max memory", s"${evt.maxMemory / 1000000} MB"),
        stat("JVM Total memory", s"${evt.totalMemory / 1000000} MB")
      )
      case FooUpdated(foo) => event("Foo Updated", ServerEventListStyle.fooUpdated)(s"Updated: $foo")
      case FooDeleted(foo) => event("Foo Deleted", ServerEventListStyle.fooDeleted)(s"Deleted: $foo")
    }
  }.build
}
