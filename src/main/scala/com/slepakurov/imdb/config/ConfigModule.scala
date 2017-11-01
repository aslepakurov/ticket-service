package com.slepakurov.imdb.config

import com.google.inject.{AbstractModule, Provider}
import com.slepakurov.imdb.config.ConfigModule.ConfigProvider
import com.typesafe.config.{Config, ConfigFactory}
import net.codingwell.scalaguice.ScalaModule

object ConfigModule {
  class ConfigProvider extends Provider[Config] {
    override def get(): Config = {
      ConfigFactory.load()
    }
  }
}

/**
 * Binds the application configuration to the [[Config]] interface.
 *
 * The config is bound as an eager singleton so that errors in the config are detected
 * as early as possible.
 */
class ConfigModule extends AbstractModule with ScalaModule {

  override def configure() {
    bind[Config].toProvider[ConfigProvider].asEagerSingleton()
  }

}
