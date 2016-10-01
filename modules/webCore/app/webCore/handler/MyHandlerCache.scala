package webCore.handler

import javax.inject.{ Inject, Singleton }

import be.objectify.deadbolt.scala.{DeadboltHandler, HandlerKey}
import be.objectify.deadbolt.scala.cache.HandlerCache

@Singleton
class MyHandlerCache @Inject() (
  val defaultHandler: MyDeadboltHandler
) extends HandlerCache {

    // HandlerKeys is an user-defined object, containing instances
    // of a case class that extends HandlerKey
    val handlers: Map[Any, DeadboltHandler] = Map(HandlerKeys.defaultHandler -> defaultHandler,
                                                  HandlerKeys.altHandler -> defaultHandler,
                                                  HandlerKeys.userlessHandler -> defaultHandler)

    // Get the default handler.
    override def apply(): DeadboltHandler = defaultHandler

    // Get a named handler
    override def apply(handlerKey: HandlerKey): DeadboltHandler = handlers(handlerKey)
}
