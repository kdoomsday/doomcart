package core.daos

import core.models.{ UserTable, RoleTable }
import core.models.security.MySubject
import scala.concurrent.Future
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile
import javax.inject.Inject

import scala.concurrent.ExecutionContext.Implicits.global

class SubjectDaoSlick @Inject() (val dcp: DatabaseConfigProvider)
  extends SubjectDao
  with UserTable
  with RoleTable
{
  override val dc = dcp.get[JdbcProfile]
  import dc.driver.api._

  private[this] val db = dc.db
  override def subjectByIdentifier(identifier: String): Future[Option[MySubject]] = {
    db.run {
      (for {
        u <- users if u.login === identifier
        r <- roles if r.id === u.roleId
      } yield (u, r)).result.headOption
    } map ( _ map {
      case (u, r) => MySubject(u, r)
    })
  }
}
