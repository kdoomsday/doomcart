package bindings

import com.google.inject.AbstractModule
import crypto.{HashService, MessageDigestHashService}
import daos._

/**
  * User: Eduardo Barrientos
  * Date: 21/09/16
  * Time: 05:55 PM
  */
class DefaultBindings extends AbstractModule {

  def configure() = {
    bind(classOf[UserDao]).to(classOf[UserDaoSlick])
    bind(classOf[SubjectDao]) to classOf[SubjectDaoSlick]
    bind(classOf[ProductDao]) to classOf[ProductDaoSlick]
    bind(classOf[ImgSave]) to classOf[ImgSaveImpl]

    bind(classOf[HashService]) toInstance MessageDigestHashService.Sha256HashService
  }
}
