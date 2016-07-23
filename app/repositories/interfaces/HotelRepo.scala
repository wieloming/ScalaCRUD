package repositories.interfaces

import domain.DBid
import domain.hotel.Hotel

import scala.concurrent.Future

trait HotelRepo extends BaseRepo[Hotel, Hotel.Id] {

  def findAllByCity(s: Hotel.City): Future[List[Hotel with DBid[Hotel.Id]]]
}
