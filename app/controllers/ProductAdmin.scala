package controllers

import models.Notification
import play.api.mvc.Controller
import play.api.data.Forms._
import play.api.data._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import javax.inject.Inject

import actions.Actions
import daos.ProductDao

/** Controller for product actions */
class ProductAdmin @Inject() (
  actions:    Actions,
  productDao: ProductDao
) extends Controller {

  import ProductAdmin.productForm

  def addProduct = actions.roleAction("employee") { _ =>
    Future.successful( Ok(views.html.addProduct(productForm)) )
  }

  def addProductHandle = actions.roleAction("employee") { implicit request =>
    productForm.bindFromRequest.fold(
      formWithErrors => Future.successful(BadRequest(views.html.addProduct(formWithErrors))),
      info => {
        productDao.insert(info).map { p =>
          implicit val nots = Seq(Notification("success", "Product created successfully"))
          Ok(views.html.addProduct(productForm))
        }
      }
    )
  }

}

object ProductAdmin {
  import daos.ProductDao.ProductInfo

  lazy val productForm = Form(
    mapping(
      "name"  -> nonEmptyText,
      "price" -> bigDecimal(17, 2)
    )(ProductInfo.apply)(ProductInfo.unapply)
  )
}
