package daos

import scala.concurrent.Future
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile
import javax.inject.Inject
import scala.concurrent.ExecutionContext.Implicits.global

import models.{ Product, ProductTable, ProductImage, ProductImageTable }
import daos.ProductDao.ProductInfo

class ProductDaoSlick @Inject() (val dcp: DatabaseConfigProvider)
  extends ProductDao with ProductTable with ProductImageTable
{
  override val dc = dcp.get[JdbcProfile]
  import dc.driver.api._

  private[this] val db = dc.db

  override def insert(productInfo: ProductInfo): Future[Product] = {
    val p = Product(0L, name = productInfo.name, price = productInfo.price)

    val fp: Future[Product] = db.run(
      (products returning products.map(_.id) into ((product, id) => product.copy(id=id))) += p
    )

    fp.flatMap { p =>
      productInfo.url match {
        case Some(url) => db.run( productImages += ProductImage(p.id, url) ).map(_ => p)
        case None      => Future.successful(p)
      }
    }
  }
}
