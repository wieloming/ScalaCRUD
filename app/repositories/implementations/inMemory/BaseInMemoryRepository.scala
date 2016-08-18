package repositories.implementations.inMemory

import java.util.concurrent.atomic.AtomicLong

import repositories.interfaces._
import utils.ValueOrErrors
import scala.collection.breakOut

trait BaseInMemoryRepository[T <: {def id[B <: Id] : Some[B]}, Id] extends BaseRepo[T, Id] {
  type DUPA = {def id[B <: Id] : Some[B]}
  protected val db: scala.collection.concurrent.TrieMap[Id, DUPA]
  protected val idSequence: AtomicLong

  override def create(obj: Validated[T]): ValueOrErrors[Id]

  override def update(id: Id, el: Validated[T]): ValueOrErrors[FromDB[T]]

  override def findByIds(ids: List[Id]): ValueOrErrors[List[FromDB[DUPA]]] = {
    val objects = ids.map(id => (id, db.get(id)))
    val (errors, validated) = objects.foldLeft((Errors.empty, List.empty[FromDB[DUPA]])) {
      case ((errs, vs), (id, Some(t))) => (errs, vs :+ FromDB(t))
      case ((errs, vs), (id, None)) => (errs + Errors.single("No element with id: " + id), vs)
    }
    if (errors.isEmpty) ValueOrErrors.data(validated)
    else ValueOrErrors.errors(errors)
  }

  override def findById(id: Id): ValueOrErrors[FromDB[DUPA]] = {
    db.get(id) match {
      case Some(t) => ValueOrErrors.data(FromDB(t))
      case None => ValueOrErrors.errors(Errors.single("No element with id: " + id))
    }
  }

  override def findAll(): ValueOrErrors[List[FromDB[DUPA]]] = {
    ValueOrErrors.data(db.values.map(FromDB(_))(breakOut))
  }

  override def remove(id: Id): ValueOrErrors[FromDB[DUPA]] = {
    db.get(id) match {
      case Some(t) =>
        db.remove(id)
        ValueOrErrors.data(FromDB(t))
      case None => ValueOrErrors.errors(Errors.single("No element with id: " + id + " in db"))
    }
  }
}
