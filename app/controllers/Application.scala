package controllers

import scala.concurrent.Future

import be.objectify.deadbolt.scala.ActionBuilders
import javax.inject.Inject
import models._
import play.api.data._
import play.api.data.Forms._
import play.api.mvc._

import scala.language.reflectiveCalls

class Application @Inject() (val actionBuilder: ActionBuilders) extends Controller {

  /*
  //create an instance of the table
  val cats = TableQuery[CatsTable] //see a way to architect your app in the computers-database-slick sample

  //JSON read/write macro
  implicit val catFormat = Json.format[Cat]
  */

  def index = actionBuilder.SubjectPresentAction().defaultHandler() { authRequest =>
    Future.successful( Ok(views.html.index(List())) )
  }

  val catForm = Form(
    mapping(
      "name" -> text(),
      "color" -> text()
    )(Cat.apply)(Cat.unapply)
  )

  def insert = TODO

  def jsonFindAll = TODO

  def jsonInsert = TODO

}
