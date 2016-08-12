package repositories.interfaces

import domain.hotel.Hotel
import utils.ValidDataListOrErrors

trait HotelRepo extends BaseRepo[Hotel, Hotel.Id] {

  def findAllByCity(s: Hotel.City): ValidDataListOrErrors[Hotel]
}
