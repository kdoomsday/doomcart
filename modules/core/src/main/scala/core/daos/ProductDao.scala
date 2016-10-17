package core.daos

import scala.concurrent.Future
import core.models.Product

object ProductDao {
  /** Basic product information used to create it */
  case class ProductInfo(name: String, price: BigDecimal)
}


trait ProductDao {
  import ProductDao.ProductInfo

  /** Insert a new product.
    * @param productInfo Basic product information
    * @return Future with the actual product
    */
  def insert(productInfo: ProductInfo): Future[Product]
}
