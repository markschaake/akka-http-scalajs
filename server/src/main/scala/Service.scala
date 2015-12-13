package example.akkwebsockets

import akka.actor.ActorRef
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.ws.Message
import akka.http.scaladsl.model.ws.TextMessage
import akka.http.scaladsl.server.Directives._
import akka.stream.scaladsl.Flow
import akka.stream.scaladsl.Source

import upickle.default._

class Service(manager: ActorRef) {

  def serverEventsHandler: Flow[Message, Message, Any] = Flow[Message] merge {
    Source.actorPublisher[ServerEvent](ServerEventPublisher.props(manager)) map { evt =>
      TextMessage(write(evt))
    }
  }

  val route = {
    path("server-events") {
      handleWebsocketMessages(serverEventsHandler)
    } ~
    pathPrefix("api") {
      path("hi") {
        complete("TODO")
      }
    } ~
    pathEndOrSingleSlash(getFromFile("../index-dev.html")) ~
    pathPrefix("js") {
      getFromDirectory("../webclient/target/scala-2.11")
    }
  }
}
