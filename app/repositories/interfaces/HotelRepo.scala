package repositories.interfaces

import domain.hotel.Hotel
import utils.ValueOrErrors

trait HotelRepo extends BaseRepo[Hotel, Hotel.ForCreate, Hotel.ModelId] {

  def findAllByCity(s: Hotel.City): ValueOrErrors[List[Hotel]]
}
