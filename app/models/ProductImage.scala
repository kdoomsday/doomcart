package models

case class ProductImage(
  productId: Long,
  imageUrl:  String
)

trait ProductImageTable extends ProductTable {
  import dc.driver.api._

  private[ProductImageTable] class ProductImages(tag: Tag) extends Table[ProductImage](tag, "product_images") {
    def productId = column[Long]("product_id")
    def imageUrl  = column[String]("image_url")

    def product = foreignKey("fk_product_id", productId, products)(_.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Cascade)
    def key     = primaryKey("pk_product_image", (productId, imageUrl))

    def * = (productId, imageUrl) <> (ProductImage.tupled, ProductImage.unapply)
  }

  lazy val productImages = TableQuery[ProductImages]
}
