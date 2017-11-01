package com.slepakurov.imdb.rest

import akka.http.scaladsl.server.HttpApp
import com.google.inject.AbstractModule
import com.google.inject.name.Names
import net.codingwell.scalaguice.ScalaModule

class RestModule extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    bind[HttpApp].annotatedWith(Names.named(TicketRest.name)).to[TicketRest]
  }
}
