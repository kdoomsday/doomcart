package models

import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile

import be.objectify.deadbolt.scala.models.{ Role => DeadboltRole}

/** Database role */
case class Role(
  id:   Int,
  override val name: String
) extends DeadboltRole

trait RoleTable {
  val dc: DatabaseConfig[JdbcProfile]

  import dc.driver.api._

  private[RoleTable] class Roles(tag: Tag) extends Table[Role](tag, "roles") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")

    def * = (id, name) <> (Role.tupled, Role.unapply)
  }

  lazy val roles = TableQuery[Roles]
}
