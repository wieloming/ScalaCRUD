package utils

import repositories.interfaces.{Errors, Validated}

import scala.concurrent.Future

case class ValidDataOrErrors[T](value: Future[Either[Errors, Validated[T]]]) {
  def map[F](f: T => F): ValidDataOrErrors[F] =
    ValidDataOrErrors(value.map(_.right.map(el => Validated(f(el.valid)))))
}

case class OptValidDataOrErrors[T](value: Future[Either[Errors, Option[Validated[T]]]]) {
  def map[F](f: T => F): OptValidDataOrErrors[F] =
    OptValidDataOrErrors(value.map(_.right.map(_.map(el => Validated(f(el.valid))))))
}
