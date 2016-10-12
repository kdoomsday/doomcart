package webCore.controllers

import play.api.mvc.{ Action, Controller, Request }
import play.api.data._
import play.api.data.Forms._
import play.api.Logger

import core.daos.UserDao
import webCore.actions.Actions

import be.objectify.deadbolt.scala.ActionBuilders
import javax.inject.Inject

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * User: Eduardo Barrientos
  * Date: 25/09/16
  * Time: 06:42 AM
  */
class LoginController @Inject() (
    val actionBuilder: ActionBuilders,
    val userDao:       UserDao,
    val actions:       Actions
) extends Controller {

  import LoginController.loginForm

  // Go to login if there's no session, go to home if there is
  def loginPage = Action.async { implicit req =>
    isLoggedIn(req).map(_ match {
      case true  => Redirect(routes.Application.index)
      case false => Ok(webCore.views.html.security.login(loginForm))
    })
  }

  /** Check whether there's a user logged in */
  private[this] def isLoggedIn(req: Request[_]): Future[Boolean] = {
    req.session.get("login").fold(Future.successful(false)) { login =>
      userDao.byLogin(login).map { oUser =>
        oUser.fold(false) { user =>
          actions.isStillIn(user)
        }
      }
    }
  }


  /** Handle the user logging in */
  def login = Action.async { implicit request =>
    Logger.info("Attempting login")
    loginForm.bindFromRequest.fold(
      formWithErrors => Future( BadRequest(webCore.views.html.security.login(formWithErrors)) ),

      userData => {
        val (login, pwd) = userData
        authenticate(login, pwd) flatMap (valid =>
                          if (valid) {
                            Logger.debug(s"User $login is valid")
                            userDao.updateConnected(login) map (_ =>
                              Redirect(routes.Application.index).withSession("login" -> login)
                            )
                          }
                          else {
                            implicit val errors = Seq("Invalid user: $login")
                            Future.successful( BadRequest(webCore.views.html.security.login(loginForm)) )
                          }
                        )
      }
    )
  }


  def logout = Action {
    Redirect(routes.LoginController.loginPage()).withNewSession
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
