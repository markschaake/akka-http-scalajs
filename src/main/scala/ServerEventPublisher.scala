package example.akkwebsockets

import akka.actor._
import akka.stream.actor.ActorPublisher

object ServerEventPublisher {
  def props(manager: ActorRef) = Props(classOf[ServerEventPublisher], manager)
}

class ServerEventPublisher(manager: ActorRef) extends ActorPublisher[ServerEvent] with ActorLogging {
  var eventCount = 0
  override def preStart(): Unit = {
    import scala.concurrent.duration._
    import context.dispatcher
    context.system.scheduler.schedule(1.second, 1.second) {
      if (eventCount % 2 == 0) {
        self ! ServerEvent.FooDeleted(s"foo: $eventCount")
      } else {
        self ! ServerEvent.FooUpdated(s"foo: ${eventCount - 1}")
      }
      eventCount += 1
    }
  }
  override def receive: Receive = {
    case event: ServerEvent => onNext(event)
  }
}
