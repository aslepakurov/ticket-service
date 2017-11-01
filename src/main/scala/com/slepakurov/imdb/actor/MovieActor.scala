package com.slepakurov.imdb.actor

import akka.actor.{Actor, ActorLogging}
import com.google.inject.Inject
import com.slepakurov.imdb.akkaguice.NamedActor
import com.slepakurov.imdb.model.MovieModels._
import com.slepakurov.imdb.service.{OmdbService, RedisService}
import play.api.libs.json._

import scala.concurrent.ExecutionContext.Implicits.global

object MovieActor extends NamedActor {

  override def name = "MovieActor"
}
class MovieActor @Inject()(redisService: RedisService, omdbService: OmdbService) extends Actor with ActorLogging {

  override def receive: Receive = {
    case Movie(imdbId, availableSeats, screenId) =>
      val movieTitle = omdbService.getTitle(imdbId)
      if (movieTitle == null || movieTitle.equals("")){
        sender() ! NoSuchMovie(imdbId)
      } else {
        val newMovie = MovieInfo(imdbId, screenId, movieTitle, availableSeats.toInt, 0)
        redisService.insert(movieKey(imdbId, screenId), Json.toJson(newMovie).toString())
        sender() ! newMovie
      }

    case GetMovie(imdbId, screenId) =>
      val _sender = sender()
      redisService.get(movieKey(imdbId, screenId)).map {
        case Some(value) => _sender ! Json.fromJson[MovieInfo](Json.parse(value)).get
        case None => _sender ! MovieNotFound(imdbId, screenId)
      }

    case DeleteMovie(imdbId, screenId) =>
      val _sender = sender()
      redisService.del(movieKey(imdbId, screenId)).foreach {
        case i if i > 0 => _sender ! MovieDeleted(imdbId, screenId)
        case _ => _sender ! MovieNotFound(imdbId, screenId)
      }

    case ReserveSeat(imdbId, screenId) =>
      val _sender = sender()
      redisService.get(movieKey(imdbId, screenId)).map {
        case Some(value) =>
          val movie = Json.fromJson[MovieInfo](Json.parse(value)).get
          if (movie.availableSeats == movie.reservedSeats) {
            _sender ! NoSeatsLeft(imdbId, screenId)
          } else {}
          val updatedMovie = MovieInfo(movie.imdbId, movie.screenId, movie.movieTitle, movie.availableSeats, movie.reservedSeats+1)
          redisService.insert(movieKey(movie.imdbId, movie.screenId), Json.toJson(updatedMovie).toString())
          _sender ! updatedMovie
        case None => _sender ! MovieNotFound(imdbId, screenId)
      }
  }

  private def movieKey(imdbId: String, screenId: String) = imdbId.concat("|").concat(screenId)
}