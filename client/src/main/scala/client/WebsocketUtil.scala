package template.client

import org.scalajs.dom.WebSocket
import org.scalajs.dom.window

object WebsocketUtil {
  def ws(endpoint: String) = {
    val loc = window.location
    val wsProtocol = if (loc.protocol == "http:") "ws:" else "wss:"
    val wsUrl = s"${wsProtocol}//${loc.host}/$endpoint"
    new WebSocket(url = wsUrl)
  }
}
