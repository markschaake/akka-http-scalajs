package template.server

import template.models.{ LogLevel, ServerLogMessage }

import akka.event.Logging.LogEvent
import akka.actor._
import akka.stream.actor.ActorPublisher

object LogEventPublisher {
  def props = Props(classOf[LogEventPublisher])
}

class LogEventPublisher extends ActorPublisher[ServerLogMessage] with ActorLogging {
  override def preStart(): Unit = {
    context.system.eventStream.subscribe(self, classOf[LogEvent])
  }
  override def receive: Receive = {
    case event: LogEvent =>
      onNext(ServerLogMessage(
        event.message.toString,
        LogLevel(event.level.asInt),
        event.timestamp
      ))
  }
}
