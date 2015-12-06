package example.akkwebsockets

import akka.actor._

object Manager {
  def props: Props = Props(classOf[Manager])

  case object Subscribe
  case object GetSystemStatus
}

class Manager extends Actor with ActorLogging {
  import Manager._

  var subscribers: Set[ActorRef] = Set()
  var eventCount = 0

  override def preStart() {
    import scala.concurrent.duration._
    import context.dispatcher
    context.system.scheduler.schedule(1.second, 10.second) {
      if (eventCount % 2 == 0) {
        self ! ServerEvent.FooDeleted(s"foo: $eventCount")
      } else {
        self ! ServerEvent.FooUpdated(s"foo: ${eventCount - 1}")
      }
      eventCount += 1
    }
    context.system.scheduler.schedule(1.seconds, 2.seconds) {
      self ! GetSystemStatus
    }
  }

  def publish(any: Any) = subscribers foreach (_ ! any)

  override def receive: Receive = {
    case Manager.Subscribe =>
      subscribers = subscribers + sender
      context.watch(sender)

    case Terminated(ref) =>
      subscribers = subscribers.filterNot(_ == ref)

    case GetSystemStatus => publish(ServerEvent.ServerStatusUpdate.now)

    case evt: ServerEvent => publish(evt)

    case any => sender ! any
  }
}
