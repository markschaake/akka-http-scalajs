package template.models

sealed trait ServerEvent

object ServerEvent {
  case class FooUpdated(foo: String) extends ServerEvent
  case class FooDeleted(foo: String) extends ServerEvent
  case class ServerStatusUpdate(
    processors: Int,
    freeMemory: Long,
    maxMemory: Long,
    totalMemory: Long
  ) extends ServerEvent

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
