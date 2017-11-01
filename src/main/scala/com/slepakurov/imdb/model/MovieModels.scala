package com.slepakurov.imdb.model

import play.api.libs.json._
import play.api.libs.functional.syntax._

object MovieModels {
  case class Movie(imdbId: String, availableSeats: String, screenId: String)
  case class MovieInfo(imdbId: String, screenId: String, movieTitle: String, availableSeats: Int, reservedSeats: Int)
  case class GetMovie(imdbId: String, screenId: String)
  case class DeleteMovie(imdbId: String, screenId: String)
  case class ReserveSeat(imdbId: String, screenId: String)
  case class NoSuchMovie(imdbId: String)
  case class MovieDeleted(imdbId: String, screenId: String)
  case class MovieNotFound(imdbId: String, screenId: String)
  case class NoSeatsLeft(imdbId: String, screenId: String)

  implicit val movieInfoReads: Reads[MovieInfo] = (
    (__ \ "imdbId").read[String] and
    (__ \ "screenId").read[String] and
    (__ \ "movieTitle").read[String] and
    (__ \ "availableSeats").read[Int] and
    (__ \ "reservedSeats").read[Int]
    )(MovieInfo)
  implicit val movieInfoFormat: OFormat[MovieInfo] = Json.format[MovieInfo]
}
