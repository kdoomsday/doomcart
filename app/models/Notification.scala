package models

/** A notification to show the user */
case class Notification(level: String, message: String)

object Notification {
  def success(msg: String) = Seq( Notification("success", msg) )
  def warn   (msg: String) = Seq( Notification("warn"   , msg) )
  def error  (msg: String) = Seq( Notification("error"  , msg) )
}
