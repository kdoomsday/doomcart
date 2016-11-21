package models

import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile

/** A product category */
case class Category(id: Int, name: String)


trait CategoryTable {
  val dc: DatabaseConfig[JdbcProfile]

  import dc.driver.api._

  private[CategoryTable] class Categories(tag: Tag)
      extends Table[Category](tag, "categories")
  {
    def id   = column[Int]("id", O.PrimaryKey)
    def name = column[String]("name")

    def * = (id, name) <> (Category.tupled, Category.unapply)
  }

  def categories = TableQuery[Categories]
}
