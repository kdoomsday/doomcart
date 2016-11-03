package audits

import java.sql.Timestamp
import java.util.Date
import javax.inject.Inject
import play.api.Logger
import play.api.db.slick.DatabaseConfigProvider
import scala.concurrent.Future
import slick.driver.JdbcProfile

import scala.concurrent.ExecutionContext.Implicits.global

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
    val event = ev(description)
    Logger.debug(description)
    db.run { events += event }.map ( _ => () )
  }

  private[this] def ev(desc: String): Event =
    Event(0L, desc, new Timestamp(new Date().getTime()))
}
