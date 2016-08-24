package domain.reservation

import domain.Model
import domain.room.Room
import domain.user.User
import org.joda.time.LocalDate
import repositories.interfaces.Validated

case class Reservation(id: Option[Reservation.ModelId], roomId: Room.ModelId, userId: User.ModelId, period: Reservation.Period) {
  //TODO
  def validate: Validated[Reservation] = Validated(this)

  def notIn(p: Reservation.Period): Boolean = {
    val bothBefore = (period.from isBefore p.from) && (period.to isBefore p.to)
    val bothAfter = (period.from isAfter p.from) && (period.to isAfter p.to)
    bothBefore || bothAfter
  }
}
case object Reservation extends Model[Reservation] {
  case class ModelId(value: Long) extends domain.ModelId
  case class Period(from: LocalDate, to: LocalDate){
    require(from isBefore to)
  }
}


