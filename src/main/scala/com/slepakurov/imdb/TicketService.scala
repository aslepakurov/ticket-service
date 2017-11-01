package com.slepakurov.imdb

import akka.actor.ActorSystem
import com.google.inject.Guice
import com.slepakurov.imdb.actor.ActorModule
import com.slepakurov.imdb.akkaguice.AkkaModule
import com.slepakurov.imdb.config.ConfigModule
import com.slepakurov.imdb.rest.{RestModule, TicketRest}
import com.slepakurov.imdb.service.{EmbeddedRedisServer, ServiceModule}
import com.typesafe.config.Config
import net.codingwell.scalaguice.InjectorExtensions._


object TicketService extends App {
  val injector = Guice.createInjector(
    new AkkaModule,
    new ActorModule,
    new ConfigModule,
    new ServiceModule,
    new RestModule
  )
  val system      = injector.instance[ActorSystem]
  val config      = injector.instance[Config]
  val ticketRoute = injector.instance[TicketRest]
  val useEmbeddedRedis: Boolean = config.getBoolean("redis.embedded")

  if (useEmbeddedRedis) {
    val server = injector.instance[EmbeddedRedisServer]
    server.getServer.start()
  }

  ticketRoute.startServer(
    config.getString("http.host"),
    config.getInt("http.port"))
}