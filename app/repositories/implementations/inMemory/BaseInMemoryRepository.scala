package repositories.implementations.inMemory

import java.util.concurrent.atomic.AtomicLong

import domain.{ModelId, ModelForCreate}
import repositories.interfaces._
import utils.ValueOrErrors
import scala.collection.breakOut

trait BaseInMemoryRepository[T, G <: ModelForCreate[T, Id], Id <: ModelId] extends BaseRepo[T, G, Id] {
  protected val db: scala.collection.concurrent.TrieMap[Id, T]
  protected val idSequence: AtomicLong

  override def create(obj: Validated[G]): ValueOrErrors[Id]

  override def update(id: Id, el: Validated[G]): ValueOrErrors[T]

  override def findByIds(ids: List[Id]): ValueOrErrors[List[T]] = {
    val objects = ids.map(id => (id, db.get(id)))
    val (errors, validated) = objects.foldLeft((Errors.empty, List.empty[T])) {
      case ((errs, vs), (id, Some(t))) => (errs, vs :+ t)
      case ((errs, vs), (id, None)) => (errs + Errors.single("No element with id: " + id), vs)
    }
    if (errors.isEmpty) ValueOrErrors.data(validated)
    else ValueOrErrors.errors(errors)
  }

  override def findById(id: Id): ValueOrErrors[T] = {
    db.get(id) match {
      case Some(t) => ValueOrErrors.data[T](t)
      case None => ValueOrErrors.errors(Errors.single("No element with id: " + id))
    }
  }

  override def findAll(): ValueOrErrors[List[T]] = {
    ValueOrErrors.data(db.map { case (i, t) => t }(breakOut))
  }

  override def remove(id: Id): ValueOrErrors[T] = {
    db.get(id) match {
      case Some(t) =>
        db.remove(id)
        ValueOrErrors.data(t)
      case None => ValueOrErrors.errors(Errors.single("No element with id: " + id + " in db"))
    }
  }
}
