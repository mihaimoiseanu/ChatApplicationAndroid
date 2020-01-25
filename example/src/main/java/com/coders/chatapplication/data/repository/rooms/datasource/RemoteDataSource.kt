package com.coders.chatapplication.data.repository.rooms.datasource

import com.coders.chatapplication.data.net.api.RoomsService
import com.coders.chatapplication.data.net.models.MessageResponse
import com.coders.chatapplication.data.net.models.RoomResponse

class RemoteDataSource(private val roomsService: RoomsService) {

	suspend fun createRoom(room: RoomResponse) = roomsService.createRoom(room)

	suspend fun getPublicRooms() = roomsService.getPublicRooms()

	suspend fun getMyRooms() = roomsService.getMyRooms()

	suspend fun getRoomMessages(roomId: Long): List<MessageResponse> =
		roomsService.getRoomMessages(roomId)
}