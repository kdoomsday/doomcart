package webCore.controllers

import play.api.mvc.Controller
import play.api.data.Forms._
import play.api.data._
import scala.concurrent.Future
import javax.inject.Inject
import webCore.actions.Actions

/** Controller for product actions */
class ProductAdmin @Inject() (
  actions: Actions
) extends Controller {

  import ProductAdmin.productForm

  def addProduct = actions.roleAction("employee") { implicit authRequest =>
    Future.successful( Ok(webCore.views.html.addProduct(productForm)) )
  }


}

object ProductAdmin {
  /** Product information used to create it */
  case class ProductInfo(name: String, price: BigDecimal)
  lazy val productForm = Form(
    mapping(
      "name"  -> nonEmptyText,
      "price" -> bigDecimal(17, 2)
    )(ProductInfo.apply)(ProductInfo.unapply)
  )
}
