package models

import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile

case class ProductCategory(
  val productId : Long,
  val categoryId: Int
)

trait ProductCategoryTable extends ProductTable with CategoryTable {
  val dc: DatabaseConfig[JdbcProfile]

  import dc.driver.api._

  private[ProductCategoryTable] class ProductCats(tag: Tag)
      extends Table[ProductCategory](tag, "product_category")
  {
    def productId  = column[Long]("product_id")
    def categoryId = column[Int] ("category_id")

    def pk = primaryKey("pk_productCategory", (productId, categoryId))
    def product =
      foreignKey("fk_prodcat_product", productId, products)(
        _.id,
        onUpdate = ForeignKeyAction.Restrict,
        onDelete = ForeignKeyAction.Cascade
      )

    def * = (productId, categoryId) <> (ProductCategory.tupled, ProductCategory.unapply)

  }

  lazy val productCategories = TableQuery[ProductCats]
}
