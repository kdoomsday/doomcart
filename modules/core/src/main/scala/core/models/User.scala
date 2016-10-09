package core.models

import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile

import java.sql.Timestamp
import org.joda.time.Instant

import be.objectify.deadbolt.scala.models.Subject

/**
  * A user in the system
  * @param id    Database identifier
  * @param login User's login
  */
case class User (
  id:           Long,
  login:        String,
  password:     String,
  connected:    Boolean,
  lastActivity: Instant
) extends Subject {
  override val identifier = login
  override val roles = List()
  override val permissions = List()
}


trait UserTable {
  val dc: DatabaseConfig[JdbcProfile]

  import dc.driver.api._

  private[UserTable] class Usuarios(tag: Tag) extends Table[User](tag, "users") {
    def id           = column[Long]  ("id", O.PrimaryKey, O.AutoInc)
    def login        = column[String]("login")
    def password     = column[String]("password")
    def connected    = column[Boolean]("connected")
    def lastActivity = column[Timestamp]("last_activity")

    def idxLogin = index("uk_login", login, unique = true)

    def * = (id, login, password, connected, lastActivity).shaped <> (userTupled, userUnapply)
  }

  def userUnapply(u: User) =
    Some((u.id, u.login, u.password, u.connected, instant2Timestamp(u.lastActivity)))

  def userTupled(row: (Long, String, String, Boolean, Timestamp)): User = {
    val (id, login, pwd, connected, lastAct) = row
    User(id, login, pwd, connected, timestamp2Instant(lastAct))
  }

  // Conversions
  def instant2Timestamp(i: Instant): Timestamp = new Timestamp(i.getMillis())
  def timestamp2Instant(ts: Timestamp): Instant = new Instant(ts.getTime())


  val users = TableQuery[Usuarios]
}
