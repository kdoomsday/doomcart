package core.models.security

import core.models.{ User, Role }
import be.objectify.deadbolt.scala.models.Subject

/** Deadbolt subjects in the system. */
case class MySubject(
  user: User,
  role: Role
) extends Subject {
  override val identifier = user.login
  override val roles = List(role)
  override val permissions = List()
}
