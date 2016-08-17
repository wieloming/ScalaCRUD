package services.room

import domain.hotel.Hotel
import domain.reservation.Reservation
import domain.room.Room
import repositories.interfaces.{RoomRepo, FromDB}
import services.reservation.ReservationService
import utils.ValueOrErrors


class RoomService(reservationService: ReservationService, roomRepository: RoomRepo) {

  def remove(id: Room.Id): ValueOrErrors[FromDB[Room]] = roomRepository.remove(id)

  def create(room: Room): ValueOrErrors[FromDB[Room]] = roomRepository.create(room.validate)

  def findByIds(ids: List[Room.Id]): ValueOrErrors[List[FromDB[Room]]] =
    roomRepository.findByIds(ids)

  def findById(id: Room.Id): ValueOrErrors[FromDB[Room]] =
    roomRepository.findById(id)

  def isFreeBetween(roomId: Room.Id, period: Reservation.Period): ValueOrErrors[Boolean] = {
    reservationService.findAllByRoomId(roomId).map(!_.exists(!_.notIn(period)))
  }

  def findAllByHotelIds(ids: Hotel.Id*): ValueOrErrors[List[FromDB[Room]]] =
    roomRepository.findAll() map (_.filter(r => ids contains r.hotelId))

}
