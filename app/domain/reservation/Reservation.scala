package domain.reservation

import domain.{ModelForCreate, WithId, Model}
import domain.room.Room
import domain.user.User
import org.joda.time.LocalDate
import repositories.interfaces.Validated

case class Reservation(id: Reservation.ModelId, roomId: Room.ModelId, userId: User.ModelId, period: Reservation.Period) extends WithId [Reservation] {

  def notIn(p: Reservation.Period): Boolean = {
    val bothBefore = (period.from isBefore p.from) && (period.to isBefore p.to)
    val bothAfter = (period.from isAfter p.from) && (period.to isAfter p.to)
    bothBefore || bothAfter
  }
}
case object Reservation extends Model[Reservation] {
  case class ForCreate(roomId: Room.ModelId, userId: User.ModelId, period: Reservation.Period) extends ModelForCreate[Reservation, Reservation.ModelId] {
    //TODO
    def validate: Validated[Reservation.ForCreate] = Validated(this)
    override def toModel(id: Reservation.ModelId): Reservation = Reservation(id, roomId, userId, period)

  }
  case class ModelId(value: Long) extends domain.ModelId[Reservation]
  case class Period(from: LocalDate, to: LocalDate){
    require(from isBefore to)
  }
}


