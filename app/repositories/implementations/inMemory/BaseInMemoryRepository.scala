package repositories.implementations.inMemory

import java.util.concurrent.atomic.AtomicLong

import domain.Model
import repositories.interfaces._
import utils.ValueOrErrors
import scala.collection.breakOut

trait BaseInMemoryRepository[T, I <: Model[T]] extends BaseRepo[T, I#Id] {
  protected val db: scala.collection.concurrent.TrieMap[I#Id, T]
  protected val idSequence: AtomicLong

  override def create(obj: Validated[T]): ValueOrErrors[I#Id]

  override def update(id: I#Id, el: Validated[T]): ValueOrErrors[FromDB[T, I#Id]]

  override def findByIds(ids: List[I#Id]): ValueOrErrors[List[FromDB[T, I]]] = {
    val objects = ids.map(id => (id, db.get(id)))
    val (errors, validated) = objects.foldLeft((Errors.empty, List.empty[FromDB[T, I]])) {
      case ((errs, vs), (id, Some(t))) => (errs, vs :+ FromDB(t, id))
      case ((errs, vs), (id, None)) => (errs + Errors.single("No element with id: " + id), vs)
    }
    if (errors.isEmpty) ValueOrErrors.data(validated)
    else ValueOrErrors.errors(errors)
  }

  override def findById(id: I#Id): ValueOrErrors[FromDB[T, I]] = {
    db.get(id) match {
      case Some(t) => ValueOrErrors.data[FromDB[T, I]](FromDB(t, id))
      case None => ValueOrErrors.errors(Errors.single("No element with id: " + id))
    }
  }

  override def findAll(): ValueOrErrors[List[FromDB[T, I]]] = {
    ValueOrErrors.data(db.map { case (i, t) => FromDB(t, i) }(breakOut))
  }

  override def remove(id: I#Id): ValueOrErrors[FromDB[T, I]] = {
    db.get(id) match {
      case Some(t) =>
        db.remove(id)
        ValueOrErrors.data(FromDB(t, id))
      case None => ValueOrErrors.errors(Errors.single("No element with id: " + id + " in db"))
    }
  }
}
