package domain.hotel

import domain.{ModelForCreate, Model, WithId}
import domain.room.Room
import repositories.interfaces.Validated

case class Hotel(id: Hotel.ModelId, name: Hotel.Name, city: Hotel.City) extends WithId[Hotel] {
  def addRooms(rooms: List[Room]): HotelWithRoomsDto = HotelWithRoomsDto(this, rooms)
}

case object Hotel extends Model[Hotel] {
  case class ForCreate(name: Name, city: City) extends ModelForCreate[Hotel, Hotel.ModelId] {
    //TODO
    def validate: Validated[Hotel.ForCreate] = Validated(this)
    override def toModel(id: Hotel.ModelId): Hotel = Hotel(id, name, city)
  }
  case class ModelId(value: Long) extends domain.ModelId[Hotel]
  case class Name(value: String)
  case class City(value: String)

}


