package daos

import models.{ ProductCategory, ProductCategoryTable }
import play.api.libs.Files.TemporaryFile
import play.api.mvc.MultipartFormData.FilePart
import scala.concurrent.Future
import play.api.db.slick.DatabaseConfigProvider
import slick.dbio.Effect.Write
import slick.driver.JdbcProfile
import javax.inject.Inject
import scala.concurrent.ExecutionContext.Implicits.global

import models._
import daos.ProductDao.ProductInfo

class ProductDaoSlick @Inject() (
  val dcp: DatabaseConfigProvider,
  val imgSave: ImgSave
) extends ProductDao
    with ProductTable
    with ProductImageTable
    with ProductCategoryTable
    with CategoryTable
{
  override val dc = dcp.get[JdbcProfile]
  import dc.driver.api._

  private[this] val db = dc.db


  override def insert(productInfo: ProductInfo): Future[Product] =
    db.run( insProduct(productInfo).map(pr => pr) )


  override def addImage(productId: Long, filePart: FilePart[TemporaryFile])
      : Future[ProductImage] =
  {
    db.run( insImage(productId, imgSave.save(productId, filePart)) )
  }

  override def insertWithImage(pInfo: ProductInfo, filePart: FilePart[TemporaryFile])
      : Future[(Product, ProductImage)] =
  {
    val action =
      ( for {
        p <- insProduct(pInfo)
        pimage <- insImage(p.id, imgSave.save(p.id, filePart))
      } yield (p, pimage) ).transactionally

    db.run( action )
  }

  /** Action to insert a product given it's info, returning the resulting Product instance */
  private[this] def insProduct(pinfo: ProductInfo): DBIOAction[Product, NoStream, Write] = {
    def insCats(prodId: Long, cats: Seq[Int]) =
      productCategories ++= cats.map(c => ProductCategory(prodId, c))

    val p = Product(
      0L,
      name = pinfo.name,
      price = pinfo.price,
      description = pinfo.description
    )

    val q = ( products returning products.map(_.id) ) += p
    q.flatMap(id => insCats(id, pinfo.categories).map( _ => p.copy(id=id) ) )
  }

  private[this] def insImage(pid: Long, url: String): DBIOAction[ProductImage, NoStream, Write] = {
    val pi = ProductImage(pid, url)
    ( productImages += pi ).map(_ => pi)
  }


  def product(pid: Long): Future[(Product, Seq[ProductImage], Seq[Category])] = {
    val qprod   = products.filter(_.id === pid).result.head
    val pimages = productImages.filter(_.productId === pid).result
    val pcats   = (for {
                    pc <- productCategories if pc.productId === pid
                    c  <- categories if c.id === pc.categoryId
                  } yield c).result

    db.run {
      for {
        prod <- qprod
        pis  <- pimages
        pcs  <- pcats
      } yield (prod, pis, pcs)
    }
  }

  def all: Future[Seq[Product]] = {
    db.run( products.result )
  }
}
