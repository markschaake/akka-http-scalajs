package example.akkwebsockets

import spray.json.DefaultJsonProtocol
import spray.json._

object JsonImplicits {
  implicit class RichJsValue(val js: JsValue) extends AnyVal {
    def pack(keyValue: (String, JsValue)): JsObject = JsObject(js.asJsObject.fields + keyValue)

    def pack[A : JsonWriter](keyValue: (String, A)): JsObject = {
      val (key, a) = keyValue
      JsObject(js.asJsObject.fields + (key -> a.toJson))
    }
  }
}

trait JsonProtocol extends DefaultJsonProtocol {
  import JsonImplicits._

  implicit val fooUpdatedFormat = jsonFormat1(ServerEvent.FooUpdated)
  implicit val fooDeletedFormat = jsonFormat1(ServerEvent.FooDeleted)
  implicit val serverStatusUpdate = jsonFormat4(ServerEvent.ServerStatusUpdate)
  implicit object ServerEventWriter extends RootJsonWriter[ServerEvent] {
    import ServerEvent._
    def packType[A <: ServerEvent : JsonWriter](a: A): JsValue =
      a.toJson.pack("eventType" -> a.eventType)

    override def write(evt: ServerEvent): JsValue = evt match {
      case e: FooUpdated => packType(e)
      case e: FooDeleted => packType(e)
      case e: ServerStatusUpdate => packType(e)
    }
  }
}

object JsonProtocol extends JsonProtocol
