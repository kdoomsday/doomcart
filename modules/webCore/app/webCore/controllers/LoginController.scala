package webCore.controllers

import play.api.mvc.{ Action, Controller, Request, AnyContent }
import play.api.data._
import play.api.data.Forms._

import core.daos.UserDao

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
    val userDao:       UserDao
) extends Controller {

  import LoginController.loginForm

  // Go to login if there's no session, go to home if there is
  def loginPage = Action.async { implicit req =>
    Future {
      if (loggedIn(req)) Redirect(routes.Application.index)
      else Ok(webCore.views.html.security.login(loginForm))
    }
  }

  /** Check whether there's a user logged in */
  private[this] def loggedIn(req: Request[AnyContent]): Boolean =
    req.session.get("login").fold(false)
      { login =>
        // TODO Check session timeout
        true
      }


  /** Handle the user logging in */
  def login = actionBuilder.SubjectNotPresentAction().defaultHandler() { implicit authRequest =>
    loginForm.bindFromRequest.fold(
      formWithErrors => Future( BadRequest(webCore.views.html.security.login(formWithErrors)) ),
      userData       => authenticate(userData._1, userData._2) flatMap (valid =>
                          if (valid) {
                            userDao.updateConnected(userData._1).map(_ =>
                              Redirect(routes.Application.index).withSession("login" -> userData._1)
                            )
                          }
                          else {
                            implicit val errors = Seq("Invalid user!")
                            Future.successful( BadRequest(webCore.views.html.security.login(loginForm)) )
                          }
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
