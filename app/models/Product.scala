package models

import scala.math.BigDecimal

import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile

/** A product in the system */
case class Product (
  id          : Long,
  name        : String,
  price       : BigDecimal,
  description : String
)


trait ProductTable {
  val dc: DatabaseConfig[JdbcProfile]

  import dc.driver.api._

  private[ProductTable] class Products(tag: Tag) extends Table[Product](tag, "products") {
    def id          = column[Long]      ("id", O.PrimaryKey, O.AutoInc)
    def name        = column[String]    ("name")
    def price       = column[BigDecimal]("price")
    def description = column[String]    ("description")

    def * = (id, name, price, description) <> (Product.tupled, Product.unapply)
  }

  lazy val products = TableQuery[Products]
}
