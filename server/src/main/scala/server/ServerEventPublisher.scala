package template.server

import template.models.ServerEvent

import akka.actor._
import akka.stream.actor.ActorPublisher

object ServerEventPublisher {
  def props(manager: ActorRef) = Props(classOf[ServerEventPublisher], manager)
}

class ServerEventPublisher(manager: ActorRef) extends ActorPublisher[ServerEvent] with ActorLogging {
  override def preStart(): Unit = {
    manager ! Manager.Subscribe
    log.debug("Server event publisher created")
  }
  override def receive: Receive = {
    case event: ServerEvent => onNext(event)
  }
}
