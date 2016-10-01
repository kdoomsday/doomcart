package controllers.login

import play.api.mvc.{ Action, Controller }

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * User: Eduardo Barrientos
  * Date: 25/09/16
  * Time: 06:42 AM
  */
class LoginController extends Controller {

  def loginPage = Action.async { Future { Ok(webCore.views.html.security.login()) } }
}
