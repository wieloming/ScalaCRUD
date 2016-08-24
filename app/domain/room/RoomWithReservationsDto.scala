package domain.room

import domain.reservation.Reservation
import repositories.interfaces.FromDB

case class RoomWithReservationsDto(room: Room, reservations: List[FromDB[Reservation, Reservation.ModelId]])
