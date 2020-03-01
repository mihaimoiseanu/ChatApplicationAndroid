package com.coders.chatapplication.data.net.api

import com.coders.chatapplication.data.net.models.MessageResponse
import com.coders.chatapplication.data.net.models.RoomResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface RoomsService {

	@GET("rooms")
	suspend fun getRooms(): List<RoomResponse>

	@GET("rooms/{id}/messages")
	suspend fun getRoomMessages(@Path("id") roomId: Long): List<MessageResponse>
}