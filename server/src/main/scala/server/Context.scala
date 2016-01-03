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
  val system: ActorSystem
  def materializer: ActorMaterializer
  def manager: ActorRef
  def fooRepository: FooRepository
  def environment: Environment
}

class ProdContext(implicit val system: ActorSystem, val materializer: ActorMaterializer) extends Context {
  val config = system.settings.config
  override val manager: ActorRef = system.actorOf(Manager.props)
  override val fooRepository: FooRepository = new FooRepository
  override lazy val environment: Environment = Environment.fromString(config.getString("server.env"))
}
