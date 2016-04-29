package repositories.interfaces

import domain.hotel.Hotel

import scala.concurrent.Future

trait HotelRepo extends BaseRepo[Hotel, Hotel.id] {

  def findAllByCity(s: String): Future[List[Hotel]]
}