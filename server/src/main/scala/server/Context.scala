package template.server

import akka.actor._
import akka.stream.ActorMaterializer

trait Context {
  def system: ActorSystem
  def materializer: ActorMaterializer
  def manager: ActorRef
}

case class ProdContext(system: ActorSystem, materializer: ActorMaterializer) extends Context {
  override val manager: ActorRef = system.actorOf(Manager.props)
}
