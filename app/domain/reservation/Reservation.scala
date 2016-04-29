package domain.reservation

import domain.room.Room
import domain.user.User
import org.joda.time.LocalDate

case class Reservation(id: Option[Reservation.id], roomId: Room.id, userId: User.id, from: LocalDate, to: LocalDate) {
  require(from isBefore to)
  def notIn(fromDate: LocalDate, toDate: LocalDate): Boolean = {
    (from isBefore fromDate) && (to isBefore toDate) || ((from isAfter fromDate) && (to isAfter toDate))
  }
}
case object Reservation {
  case class id(value: Long) extends AnyVal
}


