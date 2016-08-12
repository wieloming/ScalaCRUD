package repositories.implementations.inMemory

import java.util.concurrent.atomic.AtomicLong

import repositories.interfaces.{BaseRepo, Errors, Validated}
import utils.{ValidDataListOrErrors, ValidDataOrErrors}

trait BaseInMemoryRepository[T, Id] extends BaseRepo[T, Id] {
  protected val db: scala.collection.concurrent.TrieMap[Id, T]
  protected val idSequence: AtomicLong

  override def create(obj: Validated[T]): ValidDataOrErrors[Id]
  override def update(id: Id, el: Validated[T]): ValidDataOrErrors[T]

  override def findByIds(ids: List[Id]): ValidDataListOrErrors[T] = {
    val objects = ids.map(id => (id, db.get(id)))
    val (errors, validated) = objects.foldLeft((Errors.empty, List.empty[Validated[T]])) {
      case ((errs, vs), (id, Some(t))) => (errs, vs :+ Validated(t))
      case ((errs, vs), (id, None)) => (errs + Errors.single("No element with id: " + id), vs)
    }
    if (errors.isEmpty) ValidDataListOrErrors.data(validated)
    else ValidDataListOrErrors.errors(errors)
  }
  override def findById(id: Id): ValidDataOrErrors[T] = {
    db.get(id) match {
      case Some(t) => ValidDataOrErrors.data(Validated(t))
      case None => ValidDataOrErrors.errors(Errors.single("No element with id: " + id))
    }
  }
  //TODO: breakout?
  override def findAll(): ValidDataListOrErrors[T] = {
    ValidDataListOrErrors.data(db.values.toList.map(Validated(_)))
  }
  override def remove(id: Id): ValidDataOrErrors[T] = {
    db.get(id) match {
      case Some(t) =>
        db.remove(id)
        ValidDataOrErrors.data(Validated(t))
      case None => ValidDataOrErrors.errors(Errors.single("No element with id: " + id + " in db"))
    }
  }
}
