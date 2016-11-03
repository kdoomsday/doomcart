package audits

import java.sql.Timestamp
import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile

case class Event (
  id:          Long,
  description: String,
  moment:      Timestamp
)

trait EventTable {
  val dc: DatabaseConfig[JdbcProfile]
  import dc.driver.api._

  private[EventTable] class Events(tag: Tag) extends Table[Event](tag, "events") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def description = column[String]("description")
    def moment = column[Timestamp]("moment")

    def * = (id, description, moment) <> (Event.tupled, Event.unapply)
  }

  lazy val events = TableQuery[Events]
}
