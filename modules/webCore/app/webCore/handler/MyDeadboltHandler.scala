package webCore.handler

import be.objectify.deadbolt.scala.models.Subject
import be.objectify.deadbolt.scala.{AuthenticatedRequest, DeadboltHandler, DynamicResourceHandler}
import com.google.inject.Inject
import core.daos.UserDao
import webCore.views.html.security.{denied, login}
import play.api.mvc.{Request, Result, Results}
import play.twirl.api.HtmlFormat

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class MyDeadboltHandler @Inject() (
  val userDao: UserDao
) extends DeadboltHandler {

  override def beforeAuthCheck[A](request: Request[A]): Future[Option[Result]] = Future {None}

  override def getDynamicResourceHandler[A](request: Request[A]): Future[Option[DynamicResourceHandler]] = Future {None}

  override def getSubject[A](request: AuthenticatedRequest[A]): Future[Option[Subject]] =
    request.subject match {
      case s @ Some(_) => Future(s)
      case None => request.session.get("userId") match {
        case Some(userId) => userDao.byLogin(userId)
        case None         => Future(None)
      }
    }

  override def onAuthFailure[A](request: AuthenticatedRequest[A]): Future[Result] = {
    def toContent(maybeSubject: Option[Subject]): (Boolean, HtmlFormat.Appendable) =
      maybeSubject.map(subject => (true, denied(Some(subject))))
                  .getOrElse {(false, login(getRedirectUri(request)))}

    getSubject(request).map(maybeSubject => toContent(maybeSubject))
    .map(subjectPresentAndContent =>
      if (subjectPresentAndContent._1) Results.Forbidden(subjectPresentAndContent._2)
      else Results.Unauthorized(subjectPresentAndContent._2))
  }

  private[this] def getRedirectUri[A](request: AuthenticatedRequest[A]): String = ???
}
