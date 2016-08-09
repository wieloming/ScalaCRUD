package repositories.interfaces

import domain.hotel.Hotel

import scala.concurrent.Future

trait HotelRepo extends BaseRepo[Hotel, Hotel.Id] {

  def findAllByCity(s: Hotel.City): Future[Either[Errors,List[Validated[Hotel]]]]
}
