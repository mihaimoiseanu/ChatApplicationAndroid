package com.coders.chatapplication.data.net.api

import com.coders.chatapplication.data.net.models.MessageResponse
import com.coders.chatapplication.data.net.models.RoomResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RoomsService {

	@GET("rooms/public")
	suspend fun getPublicRooms(): List<RoomResponse>

	@GET("rooms")
	suspend fun getMyRooms(): List<RoomResponse>

	@POST("rooms")
	suspend fun createRoom(@Body room: RoomResponse): RoomResponse

	@GET("rooms/{id}/messages")
	suspend fun getRoomMessages(@Path("id") roomId: Long): List<MessageResponse>
}