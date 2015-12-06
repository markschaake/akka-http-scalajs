package example.akkwebsockets

import akka.actor._
import akka.stream.actor.ActorPublisher

object ServerEventPublisher {
  def props(manager: ActorRef) = Props(classOf[ServerEventPublisher], manager)
}

class ServerEventPublisher(manager: ActorRef) extends ActorPublisher[ServerEvent] with ActorLogging {
  override def preStart(): Unit = {
    manager ! Manager.Subscribe
  }
  override def receive: Receive = {
    case event: ServerEvent => onNext(event)
  }
}
