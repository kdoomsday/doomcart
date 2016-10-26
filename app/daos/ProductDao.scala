package daos

import scala.concurrent.Future
import models.Product

object ProductDao {
  /** Basic product information used to create it */
  case class ProductInfo(name: String, price: BigDecimal, url: Option[String])
}


trait ProductDao {
  import ProductDao.ProductInfo

  /** Insert a new product.
    * @param productInfo Basic product information
    * @return Future with the actual product
    */
  def insert(productInfo: ProductInfo): Future[Product]
}
