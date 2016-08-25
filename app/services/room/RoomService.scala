package services.room

import domain.hotel.Hotel
import domain.reservation.Reservation
import domain.room.Room
import repositories.interfaces.RoomRepo
import services.reservation.ReservationService
import utils.ValueOrErrors


class RoomService(reservationService: ReservationService, roomRepository: RoomRepo) {

  def remove(id: Room.ModelId): ValueOrErrors[Room] = roomRepository.remove(id)

  def create(room: Room.ForCreate): ValueOrErrors[Room.ModelId] = roomRepository.create(room.validate)

  def findByIds(ids: List[Room.ModelId]): ValueOrErrors[List[Room]] =
    roomRepository.findByIds(ids)

  def findById(id: Room.ModelId): ValueOrErrors[Room] =
    roomRepository.findById(id)

  def isFreeBetween(roomId: Room.ModelId, period: Reservation.Period): ValueOrErrors[Boolean] = {
    reservationService.findAllByRoomId(roomId).map(!_.exists(!_.notIn(period)))
  }

  def findAllByHotelIds(ids: Hotel.ModelId*): ValueOrErrors[List[Room]] =
    roomRepository.findAll() map (_.filter(r => ids contains r.hotelId))

}
