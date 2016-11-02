package audits

import play.api.db.slick.DatabaseConfigProvider

/** Event dao that writes to the database using slick.
  * Every event is also written to the log with the same description.
  */
class EventDaoSlick @Inject() (
  val dcp: DatabaseConfigProvider
) extends EventDao with EventTable {
  override val dc = dcp.get[JdbcProfile]
  import dc.driver.api._

  private[this] val db = dc.db

  override def write(description: String): Future[Unit] = {
    val event = Event(description)
    Logger.debug(description)
    db.run { events += event }.map ( _ => () )
  }
}
