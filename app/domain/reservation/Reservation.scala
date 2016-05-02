package domain.reservation

import domain.room.Room
import domain.user.User
import org.joda.time.LocalDate

case class Reservation(id: Option[Reservation.id], roomId: Room.id, userId: User.id, period: Reservation.period) {
  def notIn(p: Reservation.period): Boolean = {
    val bothBefore = (period.from isBefore p.from) && (period.to isBefore p.to)
    val bothAfter = (period.from isAfter p.from) && (period.to isAfter p.to)
    bothBefore || bothAfter
  }
}
case object Reservation {
  case class id(value: Long) extends AnyVal
  case class period(from: LocalDate, to: LocalDate){
    require(from isBefore to)
  }
}


