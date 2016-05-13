package services.hotel

import domain.hotel.{Hotel, HotelWithRoomsDto}
import domain.room.Room
import play.api.test.PlaySpecification
import utils.TestContainer

class HotelServiceTest extends PlaySpecification with TestContainer {

  "HotelService" should {
    "return hotel Id after creation" in {
      val created = await(hotelService.createHotel(hotel))
      hotelId must equalTo(created)
    }
  }
  "return hotel from DB after creating" in {
    val fromDB = await(hotelService.findById(hotelId)).get
    HotelWithRoomsDto(Hotel(Some(Hotel.id(1L)), hotelName, city), List.empty) must equalTo(fromDB)
  }
  "HotelService" should {
    "register new room" in {
      await(hotelService.registerRoom(hotelId, room)).get must equalTo(roomId1)
    }
    "find hotel with room after registration" in {
      val hotelWithRoom = HotelWithRoomsDto(Hotel(Some(hotelId), hotelName, city), List(Room(Some(roomId1),roomPrice, hotelId)))
      await(hotelService.findById(hotelId)).get must equalTo(hotelWithRoom)
    }
  }
  "HotelService" should {
    "remove room from hotel" in {
      val hotelWithoutRoom = HotelWithRoomsDto(Hotel(Some(hotelId), hotelName, city), List.empty)
      await(hotelService.removeRoom(hotelId, roomId1)).get must equalTo(hotelWithoutRoom)
    }
    "find empty hotel after removing room" in {
      val hotelWithoutRoom = HotelWithRoomsDto(Hotel(Some(hotelId), hotelName, city), List.empty)
      await(hotelService.findById(hotelId)).get must equalTo(hotelWithoutRoom)
    }
  }
  "HotelService" should {
    "find not booked room" in {
      await(hotelService.registerRoom(hotelId, room))
      val fromDB = await(hotelService.findAvailableRooms(oneWeekPeriod, city, roomPrice - Room.price(10)))
      val roomWithId = Room(Some(roomId2), roomPrice, hotelId)
      fromDB must equalTo(List(roomWithId))
    }
  }
  "HotelService" should {
    "not found if price to high" in {
      val highPrice = roomPrice + Room.price(10)
      val fromDB = await(hotelService.findAvailableRooms(oneWeekPeriod, city, highPrice))
      fromDB must equalTo(List.empty)
    }
  }
  "BookService" should {
    "not found if city wrong" in {
      val wrongCity = "city2"
      val fromDB = await(hotelService.findAvailableRooms(oneWeekPeriod, wrongCity, roomPrice))
      fromDB must equalTo(List.empty)
    }
  }
}
