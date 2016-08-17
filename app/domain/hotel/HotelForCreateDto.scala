package domain.hotel

import repositories.interfaces.Validated


case class HotelForCreateDto(name: Hotel.Name, city: Hotel.City) {
  def toValidHotel: Validated[Hotel] = Hotel(None, name, city).validate
}
