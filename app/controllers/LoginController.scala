package controllers

import models.Notification
import play.api.mvc.{Action, Controller, Request}
import play.api.data._
import play.api.data.Forms._
import play.api.Logger
import play.api.i18n.{ I18nSupport, MessagesApi }
import be.objectify.deadbolt.scala.ActionBuilders
import javax.inject.Inject
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import daos.UserDao
import audits.EventDao
import actions.Actions
import crypto.HashService


/**
  * User: Eduardo Barrientos
  * Date: 25/09/16
  * Time: 06:42 AM
  */
class LoginController @Inject() (
    val actionBuilder: ActionBuilders,
    val userDao:       UserDao,
    val eventDao:      EventDao,
    val actions:       Actions,
    val hashService:   HashService,
    val messagesApi:   MessagesApi
) extends Controller with I18nSupport {

  import LoginController.loginForm

  // Go to login if there's no session, go to home if there is
  def loginPage = Action.async { implicit req =>
    isLoggedIn(req).map(_ match {
      case true  => Redirect(routes.Application.index)
      case false => Ok(views.html.security.login(loginForm))
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
    Logger.info(messagesApi("LoginController.login.info"))
    loginForm.bindFromRequest.fold(
      formWithErrors => Future( BadRequest(views.html.security.login(formWithErrors)) ),

      userData => {
        val (login, pwd) = userData
        authenticate(login, pwd) flatMap (valid =>
          if (valid) {
            eventDao.write( messagesApi("LoginController.login.aud.success", login) )
            userDao.updateConnected(login) map (_ =>
              Redirect(routes.Application.index).withSession("login" -> login)
            )
          }
          else {
            eventDao.write( messagesApi("LoginController.login.aud.error", login) )
            implicit val errors = Notification.error( messagesApi("LoginController.login.authError", login) )
            Future.successful( BadRequest(views.html.security.login(loginForm)) )
          }
        )
      }
    )
  }


  def logout = Action { request =>
    eventDao.write( messagesApi("LoginController.logout.aud", request.session("login")) )
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
        user.password == hashService.hashString(password, user.salt)
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
