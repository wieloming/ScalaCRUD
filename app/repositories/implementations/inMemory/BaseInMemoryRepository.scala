package repositories.implementations.inMemory

import java.util.concurrent.atomic.AtomicLong

import repositories.interfaces._
import utils.ValueOrErrors

trait BaseInMemoryRepository[T, Id] extends BaseRepo[T, Id] {
  protected val db: scala.collection.concurrent.TrieMap[Id, T]
  protected val idSequence: AtomicLong

  override def create(obj: Validated[T]): ValueOrErrors[Id]
  override def update(id: Id, el: Validated[T]): ValueOrErrors[FromDB[T]]

  override def findByIds(ids: List[Id]): ValueOrErrors[List[FromDB[T]]] = {
    val objects = ids.map(id => (id, db.get(id)))
    val (errors, validated) = objects.foldLeft((Errors.empty, List.empty[FromDB[T]])) {
      case ((errs, vs), (id, Some(t))) => (errs, vs :+ FromDB(t))
      case ((errs, vs), (id, None)) => (errs + Errors.single("No element with id: " + id), vs)
    }
    if (errors.isEmpty) ValueOrErrors.data(validated)
    else ValueOrErrors.errors(errors)
  }
  override def findById(id: Id): ValueOrErrors[FromDB[T]] = {
    db.get(id) match {
      case Some(t) => ValueOrErrors.data(FromDB(t))
      case None => ValueOrErrors.errors(Errors.single("No element with id: " + id))
    }
  }
  //TODO: breakout?
  override def findAll(): ValueOrErrors[List[FromDB[T]]] = {
    ValueOrErrors.data(db.values.toList.map(FromDB(_)))
  }
  override def remove(id: Id): ValueOrErrors[FromDB[T]] = {
    db.get(id) match {
      case Some(t) =>
        db.remove(id)
        ValueOrErrors.data(FromDB(t))
      case None => ValueOrErrors.errors(Errors.single("No element with id: " + id + " in db"))
    }
  }
}
