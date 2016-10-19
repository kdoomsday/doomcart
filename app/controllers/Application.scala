package controllers

import play.api.mvc.{ Controller, Action }
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import javax.inject.Inject
import be.objectify.deadbolt.scala.ActionBuilders

import actions.Actions


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
      val login = authRequest.subject.map(s => s.identifier).getOrElse("unknown")
      Ok(views.html.employeeIndex(login))
    }
  }
}
