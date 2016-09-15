package controllers

import models._
import play.api._
import play.api.db.slick._
import play.api.db.slick.Config.driver.simple._
import play.api.data._
import play.api.data.Forms._
import play.api.mvc._

import scala.concurrent.Future
//import play.api.Play.current
import play.api.mvc.BodyParsers._
import play.api.libs.json.Json
import play.api.libs.json.Json._

class Application extends Controller {

  /*
  //create an instance of the table
  val cats = TableQuery[CatsTable] //see a way to architect your app in the computers-database-slick sample  

  //JSON read/write macro
  implicit val catFormat = Json.format[Cat]
  */

  def index = Action.async {
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
