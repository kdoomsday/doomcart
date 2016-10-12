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

  /** Add session timeout checking to an action */
  def timedAction(block: AuthenticatedRequest[_] => Future[Result]) =
    deadbolt.SubjectPresent()() { authRequest =>
      userDao.byLogin(authRequest.session.get("login").get) flatMap { ou =>
        ou.fold(notLoggedIn)(u =>
          if ( isStillIn(u) ) {
            userDao.updateConnected(u.login) flatMap (_ => block(authRequest) )
          }
          else notLoggedIn
        )
      }
    }

  /** Whether a user is connected and not timed out */
  def isStillIn(u: User): Boolean =
    u.connected &&
    u.lastActivity.fold(false)(_ + timeout >= Instant.now())

  /** What to do if not logged in */
  private[this] def notLoggedIn: Future[Result] =
    Future.successful( Redirect(webCore.controllers.routes.LoginController.loginPage) )
}
