package com.coders.chatapplication.domain.repository

import com.coders.chatapplication.domain.model.RoomModel
import kotlinx.coroutines.flow.Flow

interface RoomRepository {
	suspend fun createRoom(name: String, isPrivate: Boolean = false): RoomModel

	suspend fun getPublicRooms(): Flow<List<RoomModel>>

	suspend fun getMyRooms(): Flow<List<RoomModel>>

	suspend fun updatePublicRooms()

	suspend fun updateMyRoom()

}