package com.slepakurov.imdb.akkaguice

import akka.actor.ActorSystem
import com.google.inject.{AbstractModule, Inject, Injector, Provider}
import com.slepakurov.imdb.akkaguice.AkkaModule.ActorSystemProvider
import com.typesafe.config.Config
import net.codingwell.scalaguice.ScalaModule

object AkkaModule {
  class ActorSystemProvider @Inject() (val config: Config, val injector: Injector) extends Provider[ActorSystem] {
    override def get() = {
      val system = ActorSystem("ticket-main-system", config)
      // add the GuiceAkkaExtension to the system, and initialize it with the Guice injector
      GuiceAkkaExtension(system).initialize(injector)
      system
    }
  }
}

/**
 * A module providing an Akka ActorSystem.
 */
class AkkaModule extends AbstractModule with ScalaModule {

  override def configure() {
    bind[ActorSystem].toProvider[ActorSystemProvider].asEagerSingleton()
  }
}
