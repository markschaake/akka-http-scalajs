package example.akkwebsockets

sealed abstract class ServerEvent(val eventType: String)

object ServerEvent {
  case class FooUpdated(foo: String) extends ServerEvent("FooUpdated")
  case class FooDeleted(foo: String) extends ServerEvent("FooDeleted")
  case class ServerStatusUpdate(
    processors: Int,
    freeMemory: Long,
    maxMemory: Long,
    totalMemory: Long
  ) extends ServerEvent("ServerStatusUpdate")

  object ServerStatusUpdate {
    def now = {
      val runtime = Runtime.getRuntime()
      ServerStatusUpdate(
        processors = runtime.availableProcessors(),
        freeMemory = runtime.freeMemory(),
        maxMemory = runtime.maxMemory(),
        totalMemory = runtime.totalMemory()
      )
    }
  }
}
