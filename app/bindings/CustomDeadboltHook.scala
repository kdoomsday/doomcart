package com.example.modules

import be.objectify.deadbolt.scala.cache.HandlerCache
import play.api.inject.{Binding, Module}
import play.api.{Configuration, Environment}
import com.example.security.MyHandlerCache

class CustomDeadboltHook extends Module {
    override def bindings(environment: Environment, configuration: Configuration): Seq[Binding[_]] = Seq(
        bind[HandlerCache].to[MyHandlerCache]
    )
}