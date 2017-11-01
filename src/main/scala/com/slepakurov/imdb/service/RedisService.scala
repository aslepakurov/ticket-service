package com.slepakurov.imdb.service

import akka.actor.ActorSystem
import com.google.inject.Inject
import com.slepakurov.imdb.repository.Repository
import com.typesafe.config.Config
import redis.{ByteStringSerializer, RedisClient}

import scala.concurrent.Future
import scala.concurrent.duration.Duration

class RedisService @Inject()(config: Config, implicit val _system: ActorSystem) extends Repository{
  private val redisHost = config.getString("redis.host")
  private val redisPort = config.getInt("redis.port")
  private val db = RedisClient(host = redisHost, port = redisPort)

  def del(key: String): Future[Long] = db.del(key)
  def insert[V](key: String, value: V, expire: Option[Duration] = None)(implicit ev: ByteStringSerializer[V]): Future[Boolean] =
    db.set(key, value)
  def get(key: String): Future[Option[String]] = db.get[String](key)
}
