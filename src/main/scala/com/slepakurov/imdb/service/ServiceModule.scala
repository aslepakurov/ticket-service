package com.slepakurov.imdb.service

import com.google.inject.AbstractModule
import com.slepakurov.imdb.repository.Repository
import net.codingwell.scalaguice.ScalaModule

class ServiceModule extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    bind[OmdbService].asEagerSingleton()
    bind[EmbeddedRedisServer].asEagerSingleton()
    bind(classOf[Repository]).to(classOf[RedisService])
  }
}
