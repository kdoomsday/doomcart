package controllers

import java.io.File
import java.util.UUID
import models.Notification
import play.api.libs.Files.TemporaryFile
import play.api.mvc.{ Controller, MultipartFormData => MPFD }
import play.api.data.Forms._
import play.api.data._
import play.api.i18n.{ I18nSupport, MessagesApi }
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import javax.inject.Inject
import MPFD.{ FilePart => FilePart }

import actions.Actions
import daos.ProductDao

/** Controller for product actions */
class ProductAdmin @Inject() (
  val actions:    Actions,
  val productDao: ProductDao,
  val messagesApi:   MessagesApi
) extends Controller with I18nSupport {

  import ProductAdmin.productForm

  def addProduct = actions.roleAction("employee") { _ =>
    Future.successful( Ok(views.html.addProduct(productForm)) )
  }

  def addProductHandle = actions.roleActionP("employee")(parse.multipartFormData) { implicit request =>
    productForm.bindFromRequest.fold(
      formWithErrors => Future.successful(BadRequest(views.html.addProduct(formWithErrors))),
      info => {
        productDao.insert(info.copy(url = moveFile(request.body.file("image")))).map { p =>
          implicit val nots = Seq(Notification("success", messagesApi("ProductAdmin.addProductHandle.success")))
          Ok(views.html.addProduct(productForm))
        }
      }
    )
  }

  /** Backup the file to a known location.
    * @return Option[String] with the actual location, if there was a file to begin with
    */
  private[this] def moveFile(ofp: Option[FilePart[TemporaryFile]]): Option[String] = {
    ofp.map { picture =>
      val dest = new File("/home/doomsday/images/", UUID.randomUUID() + picture.filename)
      dest.mkdirs()
      picture.ref.moveTo(dest, true)
      dest.toURI().toURL().toString()
    }
  }

}

object ProductAdmin {
  import daos.ProductDao.ProductInfo

  lazy val productForm = Form(
    mapping(
      "name"  -> nonEmptyText,
      "price" -> bigDecimal(17, 2),
      "image" -> optional(text)
    )(ProductInfo.apply)(ProductInfo.unapply)
  )
}
