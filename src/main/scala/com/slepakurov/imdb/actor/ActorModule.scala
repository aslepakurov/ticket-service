package com.slepakurov.imdb.actor

import akka.actor.Actor
import com.google.inject.AbstractModule
import com.google.inject.name.Names
import net.codingwell.scalaguice.ScalaModule

class ActorModule extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    bind[Actor].annotatedWith(Names.named(MovieActor.name)).to[MovieActor]
  }
}
