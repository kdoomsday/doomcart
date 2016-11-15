package models

import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile

/** A product category */
case class Category(name: String)


trait CategoryTable {
  val dc: DatabaseConfig[JdbcProfile]

  import dc.driver.api._

  private[CategoryTable] class Categories(tag: Tag)
      extends Table[Category](tag, "categories")
  {
    def name = column[String]("name", O.PrimaryKey)

    def * = (name) <> (Category.apply, Category.unapply)
  }

  def categories = TableQuery[Categories]
}
