package controllers

import daos.ImgSave
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
  val eventDao:    EventDao,
  val imgSave:     ImgSave,
  val messagesApi: MessagesApi
) extends Controller with I18nSupport {

  import ProductAdmin.productForm

  def addProduct = actions.roleAction("employee") { implicit req =>
    Future.successful( Ok(views.html.addProduct(productForm)) )
  }

  def addProductHandle = actions.roleActionP("employee")(parse.multipartFormData) { implicit request =>
    productForm.bindFromRequest.fold(
      formWithErrors => Future.successful(BadRequest(views.html.addProduct(formWithErrors))),
      info => {
        val oImage: Option[FilePart[TemporaryFile]] = request.body.file("image")
        val fp = oImage match {
          case Some(image) => productDao.insertWithImage(info, image).map { case(p, _) => p }
          case None        => productDao.insert(info)
        }

        fp map { p =>
          eventDao.write( messagesApi("ProductAdmin.addProductHandle.aud", p.id) )
          implicit val nots = Notification.success(messagesApi("ProductAdmin.addProductHandle.success"))
          Ok(views.html.addProduct(productForm))
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
}

object ProductAdmin {
  import daos.ProductDao.ProductInfo

  lazy val productForm = Form(
    mapping (
      "name"  -> nonEmptyText,
      "price" -> bigDecimal(17, 2)
    )(ProductInfo.apply)(ProductInfo.unapply)
  )
}
