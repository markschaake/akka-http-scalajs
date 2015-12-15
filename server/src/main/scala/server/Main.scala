package template.server

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory

object Main extends App {
  val config = ConfigFactory.load()
  implicit val system = ActorSystem("dsm", config)
  implicit val materializer = ActorMaterializer()
  val appContext = ProdContext(system, materializer)
  val service = new Service(appContext.manager)

  import system.dispatcher

  Http().bindAndHandle(service.route, interface = "0.0.0.0", port = 8081) onSuccess {
    case _ => system.log.info(s"HTTP server started on port 8081")
  }
}
