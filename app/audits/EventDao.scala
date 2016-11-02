package audits

/** Dao for storing and retrieving events */
trait EventDao {
  /** Write a new Event to the system */
  def write(description: String): Future[Unit]
}
