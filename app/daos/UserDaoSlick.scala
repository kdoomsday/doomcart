package daos

import javax.inject.Inject
import java.sql.Timestamp

import models.{User, UserTable}

import play.api.db.slick.DatabaseConfigProvider
import play.api.i18n.{ Lang, MessagesApi }

import slick.driver.JdbcProfile

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import play.api.Logger

/**
  * User: Eduardo Barrientos
  * Date: 20/09/16
  * Time: 04:25 PM
  */
class UserDaoSlick @Inject() (
  val dcp:         DatabaseConfigProvider,
  val messagesApi: MessagesApi
)
  extends UserDao
    with UserTable
{
  override val dc = dcp.get[JdbcProfile]
  import dc.driver.api._

  private[this] val db = dc.db

  override def byId(id: Long): Future[Option[User]] = db.run(
    users.filter(_.id === id).result.headOption
  )


  override def byLogin(login: String): Future[Option[User]] = {
    Logger.debug(messagesApi("UserDaoSlick.byLogin.debug", login))
    db.run( users.filter(_.login === login).result.headOption )
  }


  override def updateConnected(login: String): Future[Boolean] = db.run {
    val user = for (u <- users if (u.login === login)) yield (u.connected, u.lastActivity)
    user.update( (true, Option( now() )) )
  } map (_ => true)


  /** Convenience method for building a java.sql.Timestamp for right now */
  private[this] def now(): Timestamp = new Timestamp(System.currentTimeMillis())
}
