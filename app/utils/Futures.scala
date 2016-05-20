package utils

import scala.concurrent.Future
import scala.language.higherKinds
import play.api.libs.concurrent.Execution.Implicits.defaultContext

object Futures {
  object ExtendedFuture{
    def flatTraverse[A, B, F[C]]
    (in: List[A])(fn: A => Future[F[B]])(implicit ev: F[B] => Iterable[B]): Future[List[B]] =
      in.foldLeft(Future.successful[List[B]](Nil)) { (acc, el) =>
        for (r <- acc; b <- fn(el)) yield r ++ b
      }
  }

  implicit def toExtendedFuture(f: Future.type): ExtendedFuture.type = ExtendedFuture
}
