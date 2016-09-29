package core.daos

import core.models.User

import scala.concurrent.Future

/** Dao for user-related queries
  * User: Eduardo Barrientos
  * Date: 20/09/16
  * Time: 04:24 PM
  */
trait UserDao {

  def insert(login: String, password: String): Future[User]
  def byId(id: Long): Future[Option[User]]
  def byLogin(login: String): Future[Option[User]]
}
