package controllers

import domain.Id
import play.api.libs.json.{Json, Writes}
import play.api.mvc.{Controller, Result}

import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits.defaultContext

class BaseController extends Controller {

  implicit def result[T: Writes](future: Future[T]): Future[Result] =
    future.map(response => Ok(Json.toJson(response)))

  implicit def resultIfExists[T: Writes](future: Future[Option[T]]): Future[Result] =
    future.map {
      case Some(el) => Ok(Json.toJson(el))
      case _ => NotFound
    }
  implicit def resultIfNonEmpty[T: Writes](future: Future[List[T]]): Future[Result] =
    future.map {
      case Nil => NotFound
      case list => Ok(Json.toJson(list))
    }

  implicit def resultId[T <: Id](future: Future[T]): Future[Result] =
    future.map(response => Ok(Json.toJson(response.value)))
  implicit def resultIfExistsId[T <: Id](future: Future[Option[T]]): Future[Result] =
    future.map {
      case Some(el) => Ok(Json.toJson(el.value))
      case _ => NotFound
    }
}
