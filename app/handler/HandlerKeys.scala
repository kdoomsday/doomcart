package handler

import be.objectify.deadbolt.scala.HandlerKey

/**
  * User: Eduardo Barrientos
  * Date: 30/09/16
  * Time: 09:57 PM
  */
object HandlerKeys {

  val defaultHandler = Key("defaultHandler")
  val altHandler = Key("altHandler")
  val userlessHandler = Key("userlessHandler")

  case class Key(name: String) extends HandlerKey

}
