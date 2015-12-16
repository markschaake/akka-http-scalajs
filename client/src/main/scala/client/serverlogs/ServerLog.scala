package template.client.serverlogs

import japgolly.scalajs.react.BackendScope
import japgolly.scalajs.react.Callback
import japgolly.scalajs.react.ReactComponentB
import japgolly.scalajs.react.ReactElement
import japgolly.scalajs.react.vdom.prefix_<^._
import org.scalajs.dom.MessageEvent
import rx._
import template.client.StyleSheet
import template.client.WebsocketUtil
import template.models.ServerLogMessage

import scalacss.Defaults._
import scalacss.ScalaCssReact._
import upickle.default._

object ServerLogStyle extends StyleSheet {
  import dsl._
  val serverLogStyle = style(
    addClassName("server-logs")
  )
}

object ServerLogStore {

  val logMessages = Var[Vector[ServerLogMessage]](Vector.empty[ServerLogMessage])

  private val ws = WebsocketUtil.ws("server-logs")
  ws.onmessage = { evt: MessageEvent =>
    val logMessage = read[ServerLogMessage](evt.data.asInstanceOf[String])
    logMessages() = (logMessage +: logMessages())
  }
}

class ServerLogBackend(scope: BackendScope[Unit, Vector[ServerLogMessage]]) {
  Obs(ServerLogStore.logMessages) {
    if (scope.isMounted) {
      scope.setState(ServerLogStore.logMessages()).runNow()
    }
  }
  def render(state: Vector[ServerLogMessage]) = {
    <.div(ServerLogStyle.serverLogStyle,
      <.h2("Server Logs"),
      <.div(
        state.zipWithIndex map {
          case (e, i) => ServerLog.LogItem.withKey(i.toString)(e)
        }
      )
    )
  }
}

object ServerLog {
  val component = ReactComponentB[Unit]("ServerLog")
    .initialState(ServerLogStore.logMessages())
    .renderBackend[ServerLogBackend]
    .buildU

  val LogItem = ReactComponentB[ServerLogMessage]("ServerLogMessage").render { callback =>
    val evt = callback.props
    <.div(
      <.span(evt.toString)
    )
  }.build
}
