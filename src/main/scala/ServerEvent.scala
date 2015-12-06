package example.akkwebsockets

sealed abstract class ServerEvent(val eventType: String)

object ServerEvent {
  case class FooUpdated(foo: String) extends ServerEvent("FooUpdated")
  case class FooDeleted(foo: String) extends ServerEvent("FooDeleted")
}
