package com.slepakurov.imdb.rest

import java.util.concurrent.TimeUnit

import akka.actor.ActorSystem
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.{HttpApp, Route}
import akka.pattern.ask
import akka.util.Timeout
import com.google.inject.Inject
import com.slepakurov.imdb.actor.MovieActor
import com.slepakurov.imdb.akkaguice.{GuiceAkkaExtension, NamedActor}
import com.slepakurov.imdb.model.MovieModels._
import play.api.libs.json.Json

import scala.concurrent.ExecutionContextExecutor

object TicketRest extends NamedActor {
  override def name = "TicketRest"
}
class TicketRest @Inject()(implicit val system: ActorSystem)extends HttpApp with RestJsonSupport {

  private val movieHandler = system.actorOf(GuiceAkkaExtension(system).props(MovieActor.name))
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher
  implicit def requestTimeout: Timeout = Timeout(10, TimeUnit.SECONDS)


  override def routes: Route = {
    path("health") {
      get {
        complete(StatusCodes.OK -> "OK")
      }
    } ~
    pathPrefix("ticket") {
      path("buy") {
        post {
          entity(as[ReserveSeat]) { movie =>
            complete {
              (movieHandler ? movie).map {
                case movieInfo: MovieInfo =>
                  val availableSeats = if(movieInfo.reservedSeats >= movieInfo.availableSeats) "No" else s"Still ${movieInfo.availableSeats-movieInfo.reservedSeats}"
                  StatusCodes.OK -> s"Reserved seat for imdbId=${movieInfo.imdbId} screening=${movieInfo.screenId}. $availableSeats seats left."
                case NoSeatsLeft(imdbId, screenId) => StatusCodes.PreconditionFailed -> s"No seats left for $imdbId=$imdbId screening=$screenId"
                case MovieNotFound(imdbId, screenId) => StatusCodes.NotFound -> s"No movie found with imdbId=$imdbId screening=$screenId"
              }
            }
          }
        }
      }
    } ~
    pathPrefix("movie") {
      path("register") {
        post {
          entity(as[Movie]) { movie =>
            complete {
              (movieHandler ? movie).map {
                case movieInfo: MovieInfo => StatusCodes.OK -> Json.toJson(movieInfo).toString()
                case NoSuchMovie(imdbId) => StatusCodes.NotFound -> s"Movie with imdbId=$imdbId was not found."
              }
            }
          }
        }
      } ~
      path("info") {
        post {
          entity(as[GetMovie]) { movie =>
            complete{
              (movieHandler ? movie).map {
                case movieInfo: MovieInfo => StatusCodes.OK -> Json.toJson(movieInfo).toString()
                case MovieNotFound(imdbId, screenId) => StatusCodes.NotFound -> s"No movie found with imdbId=$imdbId screening=$screenId"
              }
            }
          }
        }
      } ~
      path("remove") {
        post {
          entity(as[DeleteMovie]) { movie =>
            complete{
              (movieHandler ? movie).map {
                case MovieDeleted(imdbId, screenId) => StatusCodes.OK -> s"Movie with imdbId=$imdbId screening=$screenId deleted "
                case MovieNotFound(imdbId, screenId) => StatusCodes.NotFound -> s"No movie found with imdbId=$imdbId screening=$screenId"
              }
            }
          }
        }
      }
    }
  }
}
