package controllers

import play.api.mvc.{ Controller, Action }
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import javax.inject.Inject
import be.objectify.deadbolt.scala.{ ActionBuilders, AuthenticatedRequest }

import actions.Actions
import models.Notification


class Application @Inject() (
  val actionBuilder: ActionBuilders,
  val actions: Actions
) extends Controller {

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
