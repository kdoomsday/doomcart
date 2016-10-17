package core.daos

import scala.concurrent.Future
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile
import javax.inject.Inject

import core.models.ProductTable
import core.daos.ProductDao.ProductInfo
import core.models.Product

class ProductDaoSlick @Inject() (val dcp: DatabaseConfigProvider)
  extends ProductDao with ProductTable
{
  override val dc = dcp.get[JdbcProfile]
  import dc.driver.api._

  private[this] val db = dc.db

  override def insert(productInfo: ProductInfo): Future[Product] = {
    val p = Product(0L, name = productInfo.name, price = productInfo.price)
    db.run(
      (products returning products.map(_.id) into ((product, id) => product.copy(id=id))) += p
    )
  }
}
