package domain.room

import domain.DBid
import domain.reservation.Reservation

case class RoomWithReservationsDto(room: Room with DBid[Room.Id], reservations: List[Reservation])
