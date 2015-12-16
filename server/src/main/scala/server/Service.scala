package template.server

import akka.actor.ActorRef
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.ws.Message
import akka.http.scaladsl.model.ws.TextMessage
import akka.http.scaladsl.model.{ ContentTypes, HttpEntity }
import akka.http.scaladsl.server.Directives._
import akka.stream.scaladsl.Flow
import akka.stream.scaladsl.Source
import template.models.ServerEvent
import upickle.default._

import java.time.Instant

class Service(manager: ActorRef, environment: Environment) {

  def serverEventsHandler: Flow[Message, Message, Any] = Flow[Message] merge {
    Source.actorPublisher[ServerEvent](ServerEventPublisher.props(manager)) map { evt =>
      TextMessage(write(evt))
    }
  }

  val cacheBreaker = Instant.now().toEpochMilli().toString

  val indexPage = HttpEntity(ContentTypes.`text/html`, Index(cacheBreaker, environment))

  val route = {
    path("server-events") {
      handleWebsocketMessages(serverEventsHandler)
    } ~
    pathPrefix("api") {
      path("hi") {
        complete("TODO")
      }
    } ~
    pathEndOrSingleSlash(complete(indexPage)) ~
    pathPrefix(cacheBreaker)(getFromResourceDirectory(""))
  }
}
