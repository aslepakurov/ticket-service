package com.slepakurov.imdb.repository

import redis.ByteStringSerializer

import scala.concurrent.Future
import scala.concurrent.duration.Duration

trait Repository {
  def del(key: String): Future[Long]
  def get(key: String): Future[Option[String]]
  def insert[V](key: String, value: V, expire: Option[Duration] = None)(implicit ev: ByteStringSerializer[V]): Future[Boolean]
}