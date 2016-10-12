package core.daos

import javax.inject.Inject
import java.sql.Timestamp

import core.models.{User, UserTable}

import play.api.db.slick.DatabaseConfigProvider

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
    val user = User(0L, login, password, false, null)
    (users returning users.map(_.id) into ((u, id) => u.copy(id = id))) += user
  }


  override def byId(id: Long): Future[Option[User]] = db.run(
    users.filter(_.id === id).result.headOption
  )


  override def byLogin(login: String): Future[Option[User]] = db.run(
    users.filter(_.login === login).result.headOption
  )


  override def updateConnected(login: String): Future[Unit] = db.run {
    val user = for (u <- users if (u.login === login)) yield (u.connected, u.lastActivity)
    user.update( (true, Option( now() )) ).asInstanceOf[DBIOAction[Unit, slick.dbio.NoStream, slick.dbio.Effect.Write]]
  }


  /** Convenience method for building a java.sql.Timestamp for right now */
  private[this] def now(): Timestamp = new Timestamp(System.currentTimeMillis())
}
