package example.akkwebsockets

import akka.actor._

object Manager {
  def props: Props = Props(classOf[Manager])
}

class Manager extends Actor with ActorLogging {
  override def receive: Receive = {
    case any => sender ! any
  }
}
