package handler

import be.objectify.deadbolt.scala.models.Subject
import be.objectify.deadbolt.scala.{AuthenticatedRequest, DeadboltHandler, DynamicResourceHandler}
import com.google.inject.Inject
import play.api.mvc.{Request, Result, Results}
import play.twirl.api.HtmlFormat
import play.api.i18n.{ I18nSupport, MessagesApi }

import daos.SubjectDao
import controllers.LoginController
import views.html.security.{denied, login}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class MyDeadboltHandler @Inject() (
  val subjectDao:  SubjectDao,
  val messagesApi: MessagesApi
) extends DeadboltHandler with I18nSupport {

  override def beforeAuthCheck[A](request: Request[A]): Future[Option[Result]] = Future {None}

  override def getDynamicResourceHandler[A](request: Request[A]): Future[Option[DynamicResourceHandler]] = Future {None}

  override def getSubject[A](request: AuthenticatedRequest[A]): Future[Option[Subject]] = {
    request.subject match {
      case s @ Some(_) => Future(s)
      case None => request.session.get("login") match {
        case Some(userId) => subjectDao.subjectByIdentifier(userId)
        case None         => Future(None)
      }
    }
  }

  override def onAuthFailure[A](request: AuthenticatedRequest[A]): Future[Result] = {
    def toContent(maybeSubject: Option[Subject]): (Boolean, HtmlFormat.Appendable) =
      maybeSubject.map(subject => (true, denied(Some(subject))))
                  .getOrElse { (false, login(LoginController.loginForm, getRedirectUri(request))) }

    getSubject(request).map(maybeSubject => toContent(maybeSubject))
    .map(subjectPresentAndContent =>
      if (subjectPresentAndContent._1) Results.Forbidden(subjectPresentAndContent._2)
      else Results.Unauthorized(subjectPresentAndContent._2))
  }

  private[this] def getRedirectUri[A](request: AuthenticatedRequest[A]): String =
    request.session.get("redirectUri").getOrElse("")
}
