package com.slepakurov.imdb.service

import com.google.inject.Inject
import com.typesafe.config.Config
import redis.embedded.RedisServer

class EmbeddedRedisServer @Inject()(config: Config) {
  def getServer = new RedisServer(config.getInt("redis.port"))
}
