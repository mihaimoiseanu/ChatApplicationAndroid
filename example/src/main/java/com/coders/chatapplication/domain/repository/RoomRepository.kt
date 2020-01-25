package com.coders.chatapplication.domain.repository

import com.coders.chatapplication.domain.model.MessageModel
import com.coders.chatapplication.domain.model.RoomModel

interface RoomRepository {
	suspend fun createRoom(name: String, isPrivate: Boolean = false): RoomModel
	suspend fun getPublicRooms(): List<RoomModel>
	suspend fun getMyRooms(): List<RoomModel>
	suspend fun getRoomMessages(roomId: Long): List<MessageModel>
}