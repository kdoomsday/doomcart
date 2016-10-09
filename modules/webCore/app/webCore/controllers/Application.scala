package webCore.controllers

import play.api.mvc.Controller

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.reflectiveCalls

import javax.inject.Inject

import be.objectify.deadbolt.scala.ActionBuilders


class Application @Inject() (
  val actionBuilder: ActionBuilders
) extends Controller {

  def index = actionBuilder.SubjectPresentAction().defaultHandler() { implicit authRequest =>
    Future {
      val login = authRequest.subject.map(s => s.identifier).getOrElse("unknown")
      Ok(webCore.views.html.index(login))
    }
  }
}
