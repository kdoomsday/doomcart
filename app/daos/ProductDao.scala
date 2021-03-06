package daos

import models.ProductImage
import play.api.libs.Files.TemporaryFile
import play.api.mvc.MultipartFormData.FilePart
import scala.concurrent.Future
import models.{ Product, Category }

object ProductDao {
  /** Basic product information used to create it
    * @param name        Product name
    * @param price       Product price
    * @param description Product description
    * @param categories  Seq of category ids that apply to this product
    */
  case class ProductInfo(
    name: String,
    price: BigDecimal,
    description: String,
    categories: Seq[Int]
  )
}


trait ProductDao {
  import ProductDao.ProductInfo

  /** Insert a new product.
    * @param productInfo Basic product information
    * @return Future with the actual product
    */
  def insert(productInfo: ProductInfo): Future[Product]

  /** Add an image to a product
    * @param productId Product ID
    * @param filePart  The image to save
    */
  def addImage(productId: Long, filePart: FilePart[TemporaryFile]): Future[ProductImage]

  /** Add a product with image
    * @param pInfo     Product information
    * @param filePart  The image to save
    * @return The inserted product together with it's image info.
    */
  def insertWithImage(pInfo: ProductInfo, filePart: FilePart[TemporaryFile])
      : Future[(Product, ProductImage)]

  /** Get a product by Id with it's images
    * @param pid Product ID
    */
  def product(pid: Long): Future[(Product, Seq[ProductImage], Seq[Category])]

  /** All products in the system */
  def all: Future[Seq[Product]]
}
