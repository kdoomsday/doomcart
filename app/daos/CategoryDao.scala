package daos

import scala.concurrent.Future
import models.Category

/** Dao for categories */
trait CategoryDao {
  import CategoryDao.CategoryInfo

  /** Save a category */
  def save(c: CategoryInfo): Future[Category]

  /** Get all categories */
  def all(): Future[Seq[Category]]
}

object CategoryDao {
  case class CategoryInfo(name: String)
}
