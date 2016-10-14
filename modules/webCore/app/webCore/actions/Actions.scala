package webCore.actions

import play.api.mvc.{ Controller, Result }

import core.daos.UserDao
import core.models.User

import org.joda.time.Instant
import com.github.nscala_time.time.Imports._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import be.objectify.deadbolt.scala.{ DeadboltActions, AuthenticatedRequest }

import javax.inject.Inject

class Actions @Inject() (
  deadbolt:      DeadboltActions,
  userDao:       UserDao
) extends Controller {
  val timeout = 5.minutes

  /** Applies a session timeout check. Is used for combining into other actions */
  private[this] def timeCheck(req: AuthenticatedRequest[_], block: AuthenticatedRequest[_] => Future[Result]) =
    userDao.byLogin(req.session.get("login").get) flatMap { ou =>
      ou.fold(notLoggedIn)(u =>
        if ( isStillIn(u) ) {
          userDao.updateConnected(u.login) flatMap (_ => block(req) )
        }
        else notLoggedIn
      )
  }

  /** Add session timeout checking to an action */
  def timedAction(block: AuthenticatedRequest[_] => Future[Result]) =
    deadbolt.SubjectPresent()() { authRequest =>
      timeCheck(authRequest, block)
    }

  /** Whether a user is connected and not timed out */
  def isStillIn(u: User): Boolean =
    u.connected &&
    u.lastActivity.fold(false)(_ + timeout >= Instant.now())

  /** What to do if not logged in */
  private[this] def notLoggedIn: Future[Result] =
    Future.successful( Redirect(webCore.controllers.routes.LoginController.loginPage) )


  /** Action that checks for a role and session timeout */
  def roleAction(rolename: String)(block: AuthenticatedRequest[_] => Future[Result]) =
    deadbolt.Restrict(List(Array(rolename)))() { authRequest =>
      timeCheck(authRequest, block)
    }
}
