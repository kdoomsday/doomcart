package daos

import scala.concurrent.Future

import javax.inject.Inject
import models.{Category, CategoryTable}
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile

/** Implementation of CategoryDao that uses slick for db access */
class CategoryDaoSlick @Inject() (
  val dcp: DatabaseConfigProvider
) extends CategoryDao with CategoryTable
{
  import CategoryDao.CategoryInfo

  override val dc = dcp.get[JdbcProfile]
  import dc.driver.api._

  private[this] val db = dc.db

  /* Saves the category and returns it untouched */
  override def save(c: CategoryInfo): Future[Category] = {
    val cat = Category(0, c.name)
    val query = ( categories returning categories.map(_.id) ) into ((cat, id) => cat.copy(id = id))
    db.run( query += cat )
  }


  override def all(): Future[Seq[Category]] = db.run( categories.result )
}
