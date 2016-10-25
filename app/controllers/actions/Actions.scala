package actions

import play.api.mvc.{ Action, AnyContent, BodyParser, Controller, Result }

import daos.UserDao
import models.User

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
  def timedAction[A](parser: BodyParser[A] = parse.default)
                    (block: AuthenticatedRequest[A] => Future[Result]): Action[A] =
  {
    deadbolt.SubjectPresent()(parser) { authRequest =>
      timeCheck(authRequest, block)
    }
  }

  def timedAction(block: AuthenticatedRequest[_] => Future[Result]): Action[AnyContent] =
    timedAction(parse.default)(block)

  /** Action that checks for a role and session timeout */
  def roleActionP[A](rolename: String)(parser: BodyParser[A] = parse.default)
                   (block: AuthenticatedRequest[A] => Future[Result]): Action[A] =
  {
    deadbolt.Restrict(List(Array(rolename)))(parser) { authRequest =>
      timeCheck(authRequest, block)
    }
  }


  def roleAction(rolename: String)
                (block: AuthenticatedRequest[_] => Future[Result]): Action[AnyContent] =
    roleActionP(rolename)(parse.default)(block)


  /** Applies a session timeout check. Is used for combining into other actions */
  private[this] def timeCheck[A](req: AuthenticatedRequest[A], block: AuthenticatedRequest[A] => Future[Result]) =
    userDao.byLogin(req.session.get("login").get) flatMap { ou =>
      ou.fold(notLoggedIn)(u =>
        if ( isStillIn(u) ) {
          userDao.updateConnected(u.login) flatMap (_ => block(req) )
        }
        else notLoggedIn
      )
    }

  /** Whether a user is connected and not timed out */
  def isStillIn(u: User): Boolean =
    u.connected &&
    u.lastActivity.fold(false)(_ + timeout >= Instant.now())

  /** What to do if not logged in */
  private[this] def notLoggedIn: Future[Result] =
    Future.successful( Redirect(controllers.routes.LoginController.loginPage) )


  
}
