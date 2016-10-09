package bindings

import com.google.inject.AbstractModule
import core.daos.{UserDao, UserDaoSlick}

/**
  * User: Eduardo Barrientos
  * Date: 21/09/16
  * Time: 05:55 PM
  */
class DefaultBindings extends AbstractModule {

  def configure() = {
    bind(classOf[UserDao]).to(classOf[UserDaoSlick])
  }
}
