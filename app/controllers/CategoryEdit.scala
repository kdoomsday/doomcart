package controllers

import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.{ I18nSupport, MessagesApi }
import javax.inject.Inject
import play.api.mvc.Controller

import scala.concurrent.ExecutionContext.Implicits.global

import actions.Actions
import daos.CategoryDao
import audits.EventDao
import scala.concurrent.Future


class CategoryEdit @Inject() (
  val actions:     Actions,
  val categoryDao: CategoryDao,
  val eventDao:    EventDao,
  val messagesApi: MessagesApi
) extends Controller with I18nSupport {

  // Delete a category
  def delete(id: Int) =
    categoryDao.remove(id).flatMap(_ =>
      eventDao.write(s"Category $id deleted successfully").map(_ =>
        Ok("ok") ) )


  // Form to get the category id from requests
  val catidForm = Form(
    single(
      "id" -> number
    )
  )
  def deleteAjax = actions.roleAction("employee") { implicit request =>
    catidForm.bindFromRequest.fold(
      formErrors => Future(BadRequest("error")),
        id => delete(id)
    )
  }
}
