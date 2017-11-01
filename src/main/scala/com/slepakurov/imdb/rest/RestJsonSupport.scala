package com.slepakurov.imdb.rest

import com.slepakurov.imdb.model.MovieModels._
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

trait RestJsonSupport extends DefaultJsonProtocol {
  implicit val movieFormat         : RootJsonFormat[Movie] = jsonFormat3(Movie.apply)
  implicit val getMovieFormat      : RootJsonFormat[GetMovie] = jsonFormat2(GetMovie.apply)
  implicit val movieInfoFormat     : RootJsonFormat[MovieInfo] = jsonFormat5(MovieInfo.apply)
  implicit val deleteMovieFormat   : RootJsonFormat[DeleteMovie] = jsonFormat2(DeleteMovie.apply)
  implicit val noSuchMovieFormat   : RootJsonFormat[NoSuchMovie] = jsonFormat1(NoSuchMovie.apply)
  implicit val reserveSeatFormat   : RootJsonFormat[ReserveSeat] = jsonFormat2(ReserveSeat.apply)
  implicit val noSeatsLeftFormat   : RootJsonFormat[NoSeatsLeft] = jsonFormat2(NoSeatsLeft.apply)
  implicit val movieDeletedFormat  : RootJsonFormat[MovieDeleted] = jsonFormat2(MovieDeleted.apply)
  implicit val movieNotFoundFormat : RootJsonFormat[MovieNotFound] = jsonFormat2(MovieNotFound.apply)
}
