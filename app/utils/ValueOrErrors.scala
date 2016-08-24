package utils

import repositories.interfaces.Errors

import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits._

case class ValueOrErrors[T](value: Future[Either[Errors, T]]) {
  def map[F](f: T => F): ValueOrErrors[F] =
    ValueOrErrors(value.map(_.right.map(el => f(el))))

  def flatMap[F](f: T => ValueOrErrors[F]): ValueOrErrors[F] = {
    ValueOrErrors(value.flatMap {
      case Left(errors) => Future.successful(Left(errors))
      case Right(data) => f(data).value
    })
  }

  //TODO: filter / withFilter
  //  def filter(q: T => Boolean): ValueOrErrors[T] = {
  //  }

}

object ValueOrErrors {
  def data[T](data: T) = ValueOrErrors(Future.successful(Right(data)))

  def errors[T](data: Errors) = ValueOrErrors[T](Future.successful(Left(data)))

  def traverse[T, G](list: List[G])(f: G => ValueOrErrors[T]): ValueOrErrors[List[T]] = {
    list.foldLeft(ValueOrErrors.data(List.empty[T])) { (acc, g) =>
      val either: Future[Either[Errors, List[T]] with Product with Serializable] = for {
        a <- acc.value
        b <- f(g).value
      } yield {
          (a, b) match {
            case (Left(errs1), Left(errs2)) => Left(errs1 + errs2)
            case (Left(errs), _) => Left(errs)
            case (_, Left(errs)) => Left(errs)
            case (Right(l), Right(data)) => Right(l :+ data)
          }
        }
      ValueOrErrors(either)
    }
  }
}