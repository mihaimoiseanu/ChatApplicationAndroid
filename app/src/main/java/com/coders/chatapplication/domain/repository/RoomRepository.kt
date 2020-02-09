package com.coders.chatapplication.domain.repository

import com.coders.chatapplication.domain.model.RoomModel
import kotlinx.coroutines.flow.Flow

interface RoomRepository {

	suspend fun getRooms(): Flow<List<RoomModel>>

	suspend fun getRoomWithUsers(roomId: Long): Flow<RoomModel>

	suspend fun updateRooms()

}