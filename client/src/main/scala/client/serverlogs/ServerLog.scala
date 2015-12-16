package template.client.serverlogs

import japgolly.scalajs.react.BackendScope
import japgolly.scalajs.react.Callback
import japgolly.scalajs.react.ReactComponentB
import japgolly.scalajs.react.ReactElement
import japgolly.scalajs.react.vdom.prefix_<^._
import org.scalajs.dom.MessageEvent
import rx._
import scala.scalajs.js.Date
import template.client.StyleSheet
import template.client.WebsocketUtil
import template.models.LogLevel
import template.models.ServerLogMessage

import scalacss.Defaults._
import scalacss.ScalaCssReact._
import upickle.default._

object ServerLogStyle extends StyleSheet {
  import dsl._
  val serverLogStyle = style(
    addClassName("server-logs")
  )

  private val errorColor = c"#ff0000"
  private val warningColor = c"#fff76d"
  private val infoColor = c"#aaf6ff"
  private val debugColor = c"#ffb600"

  private def colorForLevel(level: LogLevel) = level match {
    case LogLevel.Error => errorColor
    case LogLevel.Warn => warningColor
    case LogLevel.Info => infoColor
    case LogLevel.Debug => debugColor
  }

  private def mkLogMessage(level: LogLevel) = style(
    marginTop(6.px),
    unsafeChild(".level")(
      width(50.px),
      display.inlineBlock,
      textAlign.center,
      borderRadius(3.px),
      backgroundColor(colorForLevel(level)),
      marginRight(3.px),
      padding(2.px)
    ),
    unsafeChild(".time")(
      marginRight(3.px)
    ),
    unsafeChild(".message")(
      wordBreak.breakAll,
      fontStyle.italic,
      (if (level == LogLevel.Error) fontWeight.bold else fontWeight.normal)
    )
  )
  val levelStyles: Map[LogLevel, StyleA] = (Seq(LogLevel.Error, LogLevel.Warn, LogLevel.Info, LogLevel.Debug) map { level =>
    level -> mkLogMessage(level)
  }).toMap
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
    val date = new Date()
    date.setMilliseconds(evt.timestamp.toInt)
    //val style = ServerLogStyle.forLevel(evt.logLevel)
    <.div(ServerLogStyle.levelStyles(evt.logLevel),
      <.span(^.cls := "level", evt.logLevel.toString),
      <.span(^.cls := "time", date.toLocaleTimeString()),
      <.span(^.cls := "message", evt.msg)
    )
  }.build
}
