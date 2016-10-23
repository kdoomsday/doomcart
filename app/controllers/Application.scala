package controllers

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

import actions.Actions
import be.objectify.deadbolt.scala.{ActionBuilders, AuthenticatedRequest}
import javax.inject.Inject
import play.api.i18n.{MessagesApi, I18nSupport}
import play.api.mvc.{Action, Controller}


class Application @Inject() (
  val actionBuilder: ActionBuilders,
  val actions: Actions,
  val messagesApi: MessagesApi
) extends Controller with I18nSupport {

  def index = actions.timedAction { implicit authRequest =>
    Future {
      val login = authRequest.subject.map(s => s.identifier).getOrElse("unknown")
      Ok(views.html.index(login))
    }
  }

  def employeeIndex = actions.roleAction("employee") { implicit authRequest =>
    Future {
      Ok(views.html.employeeIndex(getLogin(authRequest)))
    }
  }

  private[this] def getLogin(implicit req: AuthenticatedRequest[_]): String =
    req.subject.map(s => s.identifier).getOrElse("unknown")
}
