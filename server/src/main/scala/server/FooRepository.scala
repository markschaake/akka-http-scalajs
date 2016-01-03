package template.server

import akka.actor._
import akka.pattern.ask
import akka.util.Timeout
import java.time.Instant
import scala.concurrent.Future
import scala.concurrent.duration._
import template.models.Foo

class FooRepository(implicit refFactory: ActorRefFactory) {
  import FooRepository._
  val repo = refFactory.actorOf(Props(classOf[FooRepositoryActor]))
  implicit val timeout = Timeout(5.seconds)
  def list: Future[Vector[Foo]] = (repo ? GetAll).mapTo[Vector[Foo]]
  def get(name: String): Future[Option[Foo]] = (repo ? Get(name)).mapTo[Option[Foo]]
  def add(name: String): Future[Foo] = (repo ? Add(name)).mapTo[Foo]
}

object FooRepository {
  sealed trait FooRepositoryRequest
  case object GetAll extends FooRepositoryRequest
  case class Get(name: String) extends FooRepositoryRequest
  case class Add(name: String) extends FooRepositoryRequest

  private class FooRepositoryActor extends Actor with ActorLogging {
    var foos: Vector[Foo] = Vector.empty[Foo]

    override def receive: Receive = {
      case req: FooRepositoryRequest => req match {
        case GetAll => sender ! foos
        case Get(name) => sender ! foos.find(_.name == name)
        case Add(name) =>
          val foo = foos.find(_.name == name).getOrElse {
            val f = Foo(name, Instant.now.toEpochMilli())
            foos = f +: foos.filterNot(_.name == name)
            f
          }
          sender ! foo
      }
    }
  }
}
