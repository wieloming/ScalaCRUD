package utils

import repositories.interfaces.{Errors, Validated}

import scala.concurrent.Future

case class ValidDataOrErrors[T](value: Future[Either[Errors, Validated[T]]]) {
  def map[F](f: T => F): ValidDataOrErrors[F] =
    ValidDataOrErrors(value.map(_.right.map(el => Validated(f(el.valid)))))
  def flatMap[F](f: T => Either[Errors, Validated[F]]): ValidDataOrErrors[F] = {
    val dataF = value.map {
      case Left(errors) => Left(errors)
      case Right(data) => f(data.valid)
    }
    ValidDataOrErrors(dataF)
  }
}
object ValidDataOrErrors {
  def data[T](data: Validated[T]) = ValidDataOrErrors(Future.successful(Right(data)))
  def errors[T](data: Errors) = ValidDataOrErrors[T](Future.successful(Left(data)))
}
case class ValidDataListOrErrors[T](value: Future[Either[Errors, List[Validated[T]]]]) {
  def map[F](f: T => F): ValidDataListOrErrors[F] =
    ValidDataListOrErrors(value.map(_.right.map {
      el => el.map(v => Validated(f(v.valid)))
    }))
  def flatMap[F](f: T => Either[Errors, Validated[F]]): ValidDataListOrErrors[F] = {
    val dataF = value.map {
      case Left(errors) => Left(errors)
      case Right(data) =>
        val listEither = data.map(v => f(v.valid))
        listEither.partition(_.isLeft) match {
          case (Nil, ints) => Right(for (Right(i) <- ints) yield i)
          case (errors, _) => Left(errors.foldLeft(Errors.empty) { case (acc, Left(e)) => acc + e })
        }
    }
    ValidDataListOrErrors(dataF)
  }
  def filter(q: T => Boolean): ValidDataListOrErrors[T] = {
    ValidDataListOrErrors(value.map(e => e.right.map(_.filter(d => q(d.valid)))))
  }
}
object ValidDataListOrErrors {
  def data[T](data: List[Validated[T]]) = ValidDataListOrErrors(Future.successful(Right(data)))
  def errors[T](data: Errors) = ValidDataListOrErrors[T](Future.successful(Left(data)))
}