package webCore.controllers

import play.api.mvc.{ Controller, Action }

import webCore.actions.Actions

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
//import scala.language.reflectiveCalls

import javax.inject.Inject

import be.objectify.deadbolt.scala.ActionBuilders


class Application @Inject() (
  val actionBuilder: ActionBuilders,
  val actions: Actions
) extends Controller {

  def index = actions.timedAction { implicit authRequest =>
    Future {
      val login = authRequest.subject.map(s => s.identifier).getOrElse("unknown")
      Ok(webCore.views.html.index(login))
    }
  }
}
