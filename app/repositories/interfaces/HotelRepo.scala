package repositories.interfaces

import domain.hotel.Hotel
import utils.ValueOrErrors

trait HotelRepo extends BaseRepo[Hotel, Hotel.ModelId] {

  def findAllByCity(s: Hotel.City): ValueOrErrors[List[FromDB[Hotel, Hotel.ModelId]]]
}
