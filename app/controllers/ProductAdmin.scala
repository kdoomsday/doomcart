package controllers

import daos.CategoryDao.CategoryInfo
import daos.{ CategoryDao, ImgSave }
import models.Notification
import play.api.libs.Files.TemporaryFile
import play.api.mvc.{ Controller, MultipartFormData => MPFD }
import MPFD.{ FilePart => FilePart }
import play.api.data.Forms._
import play.api.data._
import play.api.i18n.{ I18nSupport, MessagesApi }
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import javax.inject.Inject

import actions.Actions
import daos.ProductDao
import audits.EventDao

/** Controller for product actions */
class ProductAdmin @Inject() (
  val actions:     Actions,
  val productDao:  ProductDao,
  val categoryDao: CategoryDao,
  val eventDao:    EventDao,
  val imgSave:     ImgSave,
  val messagesApi: MessagesApi
) extends Controller with I18nSupport {

  import ProductAdmin.{ productForm, categoryForm }

  def addProduct = actions.roleAction("employee") { implicit req =>
    categoryDao.all().map(cats => Ok(views.html.addProduct(productForm, cats)) )
  }

  def addProductHandle = actions.roleActionP("employee")(parse.multipartFormData) { implicit request =>
    productForm.bindFromRequest.fold(
      formWithErrors => categoryDao.all().map( cats =>
                          BadRequest(views.html.addProduct(formWithErrors, cats)) ),
      info => {
        val oImage: Option[FilePart[TemporaryFile]] = request.body.file("image")
        val fp = oImage match {
          case Some(image) => productDao.insertWithImage(info, image).map { case(p, _) => p }
          case None        => productDao.insert(info)
        }

        fp flatMap { p =>
          eventDao.write( messagesApi("ProductAdmin.addProductHandle.aud", p.id) )
          implicit val nots = Notification.success(messagesApi("ProductAdmin.addProductHandle.success"))
          categoryDao.all() map (cats => Ok(views.html.addProduct(productForm, cats)))
        }
      }
    )
  }

  def addImageView(productId: Long) = actions.roleAction("employee") { implicit req =>
    Future.successful( Ok( views.html.addImageView(productId) ) )
  }

  def addImage(productId: Long) = actions.roleActionP("employee")(parse.multipartFormData) { implicit req =>
    req.body.file("image").fold {
      implicit val nots = Notification.warn( messagesApi("ProductAdmin.addImage.noImage") )
      Future.successful( Redirect( routes.Application.employeeIndex() ) )
    }{
      fpart => {
        productDao.addImage(productId, fpart).map { pi =>
          eventDao.write( messagesApi("ProductAdmin.addImage.aud", productId, pi.imageUrl) )
          Redirect(routes.Application.employeeIndex()).flashing( "success" -> messagesApi("ProductAdmin.addImage.success") )
        }
      }
    }
  }

  def productInfo(pid: Long) = actions.timedAction { implicit req =>
    productDao.product(pid).map { case (p, pimages) =>
      Ok( views.html.product.product(p, pimages) )
    }
  }

  def addCategoryView = actions.roleAction("employee") { implicit req =>
    Future.successful( Ok( views.html.product.addCategory(categoryForm) ) )
  }

  def addCategory = actions.roleAction("employee") { implicit req =>
    categoryForm.bindFromRequest.fold(
      formWithErrors =>
        Future.successful( BadRequest(views.html.product.addCategory(formWithErrors)) ),
        name => {
          eventDao.write( messagesApi("ProductAdmin.addCategory.aud", name) )
          categoryDao.save( CategoryInfo(name) )
            .map( f =>
              Redirect( routes.Application.employeeIndex() )
                .flashing("success" ->
                    messagesApi("ProductAdmin.addCategory.success", name) )
            )
        }
    )
  }

  def categories = actions.roleAction("employee") { implicit req =>
    categoryDao.all().map(cats => Ok(views.html.product.categories( cats ) ) )
  }
}

object ProductAdmin {
  import daos.ProductDao.ProductInfo

  lazy val productForm: Form[ProductInfo] = Form(
    mapping (
      "name"  -> nonEmptyText,
      "price" -> bigDecimal(17, 2),
      "description" -> text,
      "categories"  -> seq(number)
    )(ProductInfo.apply)(ProductInfo.unapply)
  )

  lazy val categoryForm: Form[String] = Form(
    single (
      "name" -> nonEmptyText
    )
  )
}
