package webCore.controllers

import play.api.mvc.{ Action, Controller }
import play.api.data._
import play.api.data.Forms._

import core.daos.UserDao
import webCore.controllers.homeChooser.HomeChooser

import be.objectify.deadbolt.scala.ActionBuilders
import javax.inject.Inject

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.reflectiveCalls

/**
  * User: Eduardo Barrientos
  * Date: 25/09/16
  * Time: 06:42 AM
  */
class LoginController @Inject() (
    val actionBuilder: ActionBuilders,
    val userDao:       UserDao,
    val homeChooser:   HomeChooser
) extends Controller {

  import LoginController.loginForm

  def loginPage = Action.async { Future { Ok(webCore.views.html.security.login(loginForm)) } }

  def login = actionBuilder.SubjectNotPresentAction().defaultHandler() { implicit authRequest =>
    loginForm.bindFromRequest.fold(
      formWithErrors => Future( BadRequest(webCore.views.html.security.login(formWithErrors)) ),
      userData       => authenticate(userData._1, userData._2) map (valid =>
                          if (valid) Redirect(homeChooser.home).withSession("login" -> userData._1)
                          else BadRequest(webCore.views.html.security.login(loginForm))
                        )
    )
  }


  /** Authenticate the user
    * @param login    User's login
    * @param password User's password
    * @return Whether the user/password combination matches a user in the system.
    */
  private[this] def authenticate(login: String, password: String): Future[Boolean] = {
    userDao.byLogin(login).map(oUser =>
      oUser.fold(false)(user =>
        user.password == password
      )
    )
  }
}

object LoginController {
  val loginForm: Form[(String, String)] = Form(
    tuple(
      "login"    -> nonEmptyText,
      "password" -> nonEmptyText
    )
  )
}
