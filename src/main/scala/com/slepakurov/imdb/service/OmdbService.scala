package com.slepakurov.imdb.service

import com.google.inject.Inject
import com.typesafe.config.Config
import play.api.libs.json._

import scalaj.http.Http

class OmdbService @Inject()(config: Config) {

  private val url    = config.getString("omdb.url")
  private val apiKey = config.getString("omdb.apiKey")

  def getTitle(imdbId: String):String = {
    val movieResponse = Http(url.format(apiKey, imdbId))
      .method("GET")
      .asString.body
    val movieJson = Json.parse(movieResponse) \ "Title"
    movieJson.getOrElse(Json.toJson("")).toString().replace("\"", "")
  }
}
