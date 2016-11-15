package daos

import scala.concurrent.Future
import models.Category

/** Dao for categories */
trait CategoryDao {
  /** Save a category */
  def save(c: Category): Future[Category]

  /** Get all categories */
  def all(): Future[Seq[Category]]
}
