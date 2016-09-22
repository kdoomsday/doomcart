package core.models

import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile

/**
  * A user in the system
  * @param id    Database identifier
  * @param login User's login
  */
case class User (
  id:       Long,
  login:    String,
  password: String
)

trait UserTable {
  val dc: DatabaseConfig[JdbcProfile]

  import dc.driver.api._

  private[UserTable] class Usuarios(tag: Tag) extends Table[User](tag, "users") {
    def id       = column[Long]  ("id", O.PrimaryKey, O.AutoInc)
    def login    = column[String]("login")
    def password = column[String]("password")

    def idxLogin = index("uk_login", login, unique = true)

    def * = (id, login, password) <> (User.tupled, User.unapply)
  }

  val usuarios = TableQuery[Usuarios]
}
