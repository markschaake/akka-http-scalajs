package example.akkwebsockets

import akka.actor.ActorRef
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.ws.Message
import akka.http.scaladsl.model.ws.TextMessage
import akka.http.scaladsl.server.Directives._
import akka.stream.scaladsl.Flow
import akka.stream.scaladsl.Source

import spray.json._

class Service(manager: ActorRef) extends JsonProtocol {

  def serverEventsHandler: Flow[Message, Message, Any] = Flow[Message] merge {
    Source.actorPublisher[ServerEvent](ServerEventPublisher.props(manager)) map { evt =>
      TextMessage(evt.toJson.compactPrint)
    }
  }

  val route = {
    path("server-events") {
      handleWebsocketMessages(serverEventsHandler)
    } ~
    pathPrefix("api") {
      path("hi") {
        complete(JsObject("message" -> "hello".toJson))
      }
    } ~
    pathEndOrSingleSlash(getFromFile("public/index.html")) ~
    getFromDirectory("public")
  }
}
