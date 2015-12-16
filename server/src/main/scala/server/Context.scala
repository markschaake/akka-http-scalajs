package template.server

import akka.actor._
import akka.stream.ActorMaterializer

sealed trait Environment
object Environment {
  case object Dev extends Environment
  case object Prod extends Environment
  def fromString(env: String): Environment = env match {
    case "dev" => Dev
    case "prod" => Prod
    case invalid => throw new IllegalArgumentException(s"Invalid environment: $invalid")
  }
}

trait Context {
  def system: ActorSystem
  def materializer: ActorMaterializer
  def manager: ActorRef
  def environment: Environment
}

case class ProdContext(system: ActorSystem, materializer: ActorMaterializer) extends Context {
  val config = system.settings.config
  override val manager: ActorRef = system.actorOf(Manager.props)
  override lazy val environment: Environment = Environment.fromString(config.getString("server.env"))
}
