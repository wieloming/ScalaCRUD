package controllers

import mappings.UtilsJson
import play.api.libs.json.{Json, Writes}
import play.api.mvc.{Controller, Result}
import repositories.interfaces.FromDB
import utils.ValueOrErrors

import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import scala.language.implicitConversions

class BaseController extends Controller with UtilsJson {

  //TODO: one method
  implicit def result[T: Writes](value: ValueOrErrors[T]): Future[Result] =
    value.value.map {
      case Right(data) => Ok(Json.toJson(data))
      case Left(errors) => NotFound(Json.toJson(errors))
    }

  implicit def resultFromDB[T: Writes](value: ValueOrErrors[FromDB[T]]): Future[Result] =
    value.value.map {
      case Right(data) => Ok(Json.toJson(data.value))
      case Left(errors) => NotFound(Json.toJson(errors))
    }


  implicit def resultListFromDB[T: Writes](value: ValueOrErrors[List[FromDB[T]]]): Future[Result] =
    value.value.map {
      case Right(data) => Ok(Json.toJson(data.map(_.value)))
      case Left(errors) => NotFound(Json.toJson(errors))
    }

}
