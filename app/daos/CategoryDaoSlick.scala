package daos

import models.{ CategoryTable, Category }
import scala.concurrent.Future
import javax.inject.Inject
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile
import scala.concurrent.ExecutionContext.Implicits.global

/** Implementation of CategoryDao that uses slick for db access */
class CategoryDaoSlick @Inject() (
  val dcp: DatabaseConfigProvider
) extends CategoryDao with CategoryTable
{
  override val dc = dcp.get[JdbcProfile]
  import dc.driver.api._

  private[this] val db = dc.db

  /* Saves the category and returns it untouched */
  override def save(c: Category): Future[Category] =
    db.run( categories += c ).map(_ => c)


  override def all(): Future[Seq[Category]] = db.run( categories.result )
}
