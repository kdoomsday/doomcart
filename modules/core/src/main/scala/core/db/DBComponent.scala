package core.db

import slick.driver.JdbcProfile

/**
  * User: Eduardo Barrientos
  * Date: 20/09/16
  * Time: 03:40 PM
  */
trait DBComponent {
  val driver: JdbcProfile

  import driver.api._

  val db: Database
}
