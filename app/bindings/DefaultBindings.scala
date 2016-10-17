package bindings

import com.google.inject.AbstractModule
import core.crypto.{HashService, MessageDigestHashService}
import core.daos.{SubjectDao, SubjectDaoSlick, UserDao, UserDaoSlick, ProductDao, ProductDaoSlick}

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

    bind(classOf[HashService]) toInstance MessageDigestHashService.Sha256HashService
  }
}
