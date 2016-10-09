package webCore.actions

import play.api.mvc.Result

import core.daos.UserDao
import core.models.User

import org.joda.time.Instant
import com.github.nscala_time.time.Imports._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.reflectiveCalls

import be.objectify.deadbolt.scala.{ ActionBuilders, AuthenticatedRequest }

import javax.inject.Inject

class Actions @Inject() (
  actionBuilder: ActionBuilders,
  userDao:       UserDao
) {
  val timeout = 5.minutes

  /** Add session timeout checking to an action */
  def timedAction(block: AuthenticatedRequest[_] => Future[Result]) =
    actionBuilder.SubjectPresentAction().defaultHandler() { implicit authRequest =>
      userDao.byLogin(authRequest.session.get("login").get) flatMap { ou =>
        ou.fold(notLoggedIn)(u =>
          if (isStillIn(u, authRequest)) {
            userDao.updateConnected(u.login). flatMap (_ => block(authRequest) )
          }
          else notLoggedIn
        )
      }
    }

  /** Whether a user is connected and not timed out */
  private[this] def isStillIn(u: User, r: AuthenticatedRequest[_]): Boolean =
    u.connected && ((u.lastActivity + timeout) >= Instant.now())

  private[this] def notLoggedIn: Future[Result] = Redirect(webCore.routes.controllers.LoginController.loginPage)
}
