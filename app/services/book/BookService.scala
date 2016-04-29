package services.book

import domain.reservation.Reservation
import domain.room.Room
import domain.user.User
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import services.reservation.ReservationService
import services.room.RoomService
import services.user.UserService

import scala.concurrent.Future

class BookService(roomService: RoomService, reservationService: ReservationService, userService: UserService) {

  def book(reservation: Reservation): Future[Option[Reservation.id]] = {
    def createReservationIfValid(user: Option[User], room: Option[Room], isFree: Option[Boolean], reservation: Reservation) =
      (user, room, isFree) match {
        case (Some(u), Some(r), Some(true)) => reservationService.create(reservation).map(Some(_))
        case _ => Future.successful(None)
      }
    for {
      user <- userService.findById(reservation.userId)
      room <- roomService.findById(reservation.roomId)
      isFree <- roomService.isFreeBetween(reservation.roomId, reservation.from, reservation.to)
      reservationId <- createReservationIfValid(user, room, isFree, reservation)
    } yield reservationId
  }
}
