package repositories.interfaces

import domain.room.Room

trait RoomRepo extends BaseRepo[Room, Room.Id]