package controllers

import play.api.libs.json.{Json, Writes}
import play.api.mvc.{Controller, Result}

import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits.defaultContext

class BaseController extends Controller {

  implicit def returnIfExists[T: Writes](future: Future[Option[T]]): Future[Result] =
    future.map { option =>
      if (option.isDefined) Ok(Json.toJson(option))
      else NotFound
    }

  implicit def returnIfNonEmpty[T: Writes](future: Future[List[T]]): Future[Result] =
    future.map { option =>
      if (option.nonEmpty) Ok(Json.toJson(option))
      else NotFound
    }

  implicit def result[T: Writes](future: Future[T]): Future[Result] =
    future.map(response => Ok(Json.toJson(response)))
}
