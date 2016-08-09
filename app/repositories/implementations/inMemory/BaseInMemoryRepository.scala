package repositories.implementations.inMemory

import java.util.concurrent.atomic.AtomicLong

import repositories.interfaces.{BaseRepo, Errors, Validated}

import scala.concurrent.Future

trait BaseInMemoryRepository[T, Id] extends BaseRepo[T, Id] {
  protected val db: scala.collection.concurrent.TrieMap[Id, T]
  protected val idSequence: AtomicLong

  override def create(obj: Validated[T]): Future[Either[Errors, Id]]
  override def update(id: Id, el: Validated[T]): Future[Either[Errors, Validated[T]]]

  override def findByIds(ids: List[Id]): Future[Either[Errors, List[Validated[T]]]] = {
    val objects = ids.map(id => (id, db.get(id)))
    val (errors, validated) = objects.foldLeft((Errors.empty, List.empty[Validated[T]])) {
      case ((errs, vs), (id, Some(t))) => (errs, vs :+ Validated(t))
      case ((errs, vs), (id, None)) => (errs + Errors.single("No element with id: " + id), vs)
    }
    if (errors.isEmpty) Future.successful(Right(validated))
    else Future.successful(Left(errors))
  }
  override def findById(id: Id): Future[Either[Errors, Option[Validated[T]]]] = {
    db.get(id) match {
      case Some(t) => Future.successful(Right(Some(Validated(t))))
      case None => Future.successful(Right(None))
    }
  }
  //TODO: breakout?
  override def findAll(): Future[Either[Errors, List[Validated[T]]]] = {
    Future.successful(Right(db.values.toList.map(Validated(_))))
  }
  override def remove(id: Id): Future[Either[Errors, Validated[T]]] = {
    db.get(id) match {
      case Some(t) =>
        db.remove(id)
        Future.successful(Right(Validated(t)))
      case None => Future.successful(Left(Errors.single("No element with id: " + id + " in db")))
    }
  }
}
