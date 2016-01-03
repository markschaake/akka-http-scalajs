package template.server

import akka.actor.ActorRef
import akka.http.scaladsl.marshalling.ToResponseMarshaller
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.marshalling.Marshaller
import akka.http.scaladsl.marshalling.Marshalling
import akka.http.scaladsl.model.HttpCharsets
import akka.http.scaladsl.model.MediaType
import akka.http.scaladsl.model.MediaTypes
import akka.http.scaladsl.model.ws.{ Message, TextMessage }
import akka.http.scaladsl.model.{ ContentTypes, HttpEntity }
import akka.http.scaladsl.server.Directives._
import akka.stream.scaladsl.{ Flow, Source }
import java.time.Instant
import template.models.{ Foo, ServerEvent, ServerLogMessage }
import upickle.default._

class Service(ctx: Context) {

  def serverEventsHandler: Flow[Message, Message, Any] = Flow[Message] merge {
    Source.actorPublisher[ServerEvent](ServerEventPublisher.props(ctx.manager)) map { evt =>
      TextMessage(write(evt))
    }
  }

  def logEventsHandler: Flow[Message, Message, Any] = Flow[Message] merge {
    Source.actorPublisher[ServerLogMessage](LogEventPublisher.props) map { evt =>
      TextMessage(write(evt))
    }
  }

  def toJson(foos: Vector[Foo]) = write(foos)

  implicit val fooMarshaller: ToResponseMarshaller[Foo] = Marshaller.opaque[Foo, HttpResponse] { foo =>
    HttpResponse(entity = HttpEntity(
      ContentTypes.`application/json`,
      write(foo)
    ))
  }

  implicit val foosMarshaller: Marshaller[Vector[Foo], HttpResponse] =
    Marshaller.opaque[Vector[Foo], HttpResponse] { foos =>
      HttpResponse(entity = HttpEntity(
        ContentTypes.`application/json`,
        write(foos)
      ))
    }

  val cacheBreaker = Instant.now().toEpochMilli().toString

  val indexPage = HttpEntity(ContentTypes.`text/html`, Index(cacheBreaker, ctx.environment))

  val route = {
    path("server-events") {
      handleWebsocketMessages(serverEventsHandler)
    } ~
    path("server-logs") {
      handleWebsocketMessages(logEventsHandler)
    } ~
    pathPrefix("api") {
      pathPrefix("foos") {
        pathEndOrSingleSlash {
          get(complete(ctx.fooRepository.list)) ~
          entity(as[String]) { name =>
            complete(ctx.fooRepository.add(name))
          }
        }
      }
    } ~
    pathEndOrSingleSlash(complete(indexPage)) ~
    pathPrefix(cacheBreaker)(getFromResourceDirectory(""))
  }
}
