package com.slepakurov.imdb

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.google.inject.Guice
import com.slepakurov.imdb.actor.ActorModule
import com.slepakurov.imdb.akkaguice.AkkaModule
import com.slepakurov.imdb.config.ConfigModule
import com.slepakurov.imdb.model.MovieModels.{GetMovie, Movie, ReserveSeat}
import com.slepakurov.imdb.rest.{RestJsonSupport, RestModule, TicketRest}
import com.slepakurov.imdb.service.ServiceModule
import com.typesafe.config.Config
import net.codingwell.scalaguice.InjectorExtensions._
import org.scalatest._
import redis.RedisClient

class TicketServiceSpec
  extends WordSpec with Matchers with ScalatestRouteTest with RestJsonSupport with BeforeAndAfter {

  private val IMDB_ID = "tt0446029"
  private val SCREENING_ID = "123456"


  private val injector = Guice.createInjector(
    new AkkaModule,
    new ActorModule,
    new RestModule,
    new ConfigModule,
    new ServiceModule
  )
  private val ticketRest = injector.instance[TicketRest]
  private val config = injector.instance[Config]

  before {
    val client = RedisClient(config.getString("redis.host"), config.getInt("redis.port"))
  }

  "The ticket Service" should {
    "save movie" in {
      Post("/movie/register", Movie(IMDB_ID, "1", SCREENING_ID)) ~> ticketRest.routes ~> check {
        status shouldEqual StatusCodes.OK
        responseAs[String].length should be > 0
        responseAs[String] shouldEqual "{\"imdbId\":\"tt0446029\",\"screenId\":\"123456\",\"movieTitle\":\"Scott Pilgrim vs. the World\",\"availableSeats\":1,\"reservedSeats\":0}"
      }
    }
  }
}