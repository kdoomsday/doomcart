package core.daos

import javax.inject.Inject

import core.models.{User, UserTable}
import play.api.db.slick.DatabaseConfigProvider
import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile

import scala.concurrent.Future

/**
  * User: Eduardo Barrientos
  * Date: 20/09/16
  * Time: 04:25 PM
  */
class UserDaoSlick @Inject() (val dcp: DatabaseConfigProvider)
  extends UserDao
    with UserTable
{
  override val dc = dcp.get[JdbcProfile]
  import dc.driver.api._

  private[this] val db = dc.db


  override def insert(login: String, password: String): Future[User] = db.run {
    val user = User(0L, login, password)
    (usuarios returning usuarios.map(_.id) into ((u, id) => u.copy(id = id))) += user
  }

  override def byId(id: Long): Future[Option[User]] = db.run(
    usuarios.filter(_.id === id).result.headOption
  )

  override def byLogin(login: String): Future[Option[User]] = db.run(
    usuarios.filter(_.login === login).result.headOption
  )
}
