package models

import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile

import java.sql.Timestamp
import org.joda.time.Instant

/**
  * A user in the system
  * @param id    Database identifier
  * @param login User's login
  */
case class User (
  id:           Long,
  login:        String,
  password:     String,
  salt:         Int,
  roleId:       Int,
  connected:    Boolean,
  lastActivity: Option[Instant]
)

trait UserTable {
  val dc: DatabaseConfig[JdbcProfile]

  import dc.driver.api._

  private[UserTable] class Usuarios(tag: Tag) extends Table[User](tag, "users") {
    def id           = column[Long]  ("id", O.PrimaryKey, O.AutoInc)
    def login        = column[String]("login")
    def password     = column[String]("password")
    def salt         = column[Int]("salt")
    def roleId       = column[Int]("role_id")
    def connected    = column[Boolean]("connected")
    def lastActivity = column[Option[Timestamp]]("last_activity")

    def idxLogin = index("uk_login", login, unique = true)

    def * = (id, login, password, salt, roleId, connected, lastActivity).shaped <> (userTupled, userUnapply)
  }

  // User -> Option[Tuple]
  def userUnapply(u: User) =
    Some((u.id, u.login, u.password, u.salt, u.roleId, u.connected, u.lastActivity.map(instant2Timestamp)))

  // Tuple -> User
  def userTupled(row: (Long, String, String, Int, Int, Boolean, Option[Timestamp])): User = {
    val (id, login, pwd, salt, roleId, connected, oLastAct) = row
    User(id, login, pwd, salt, roleId, connected, oLastAct.map(timestamp2Instant))
  }

  // Conversions
  def instant2Timestamp(i: Instant): Timestamp = new Timestamp(i.getMillis())
  def timestamp2Instant(ts: Timestamp): Instant = new Instant(ts.getTime())


  lazy val users = TableQuery[Usuarios]
}
