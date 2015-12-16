package template.models

sealed trait LogLevel
object LogLevel {
  case object Error extends LogLevel
  case object Warn extends LogLevel
  case object Info extends LogLevel
  case object Debug extends LogLevel

  def apply(intValue: Int): LogLevel = intValue match {
    case 1 => Error
    case 2 => Warn
    case 3 => Info
    case 4 => Debug
    case other => throw new IllegalArgumentException(s"Invalid log level: $other")
  }
}

case class ServerLogMessage(msg: String, logLevel: LogLevel, timestamp: Long)
