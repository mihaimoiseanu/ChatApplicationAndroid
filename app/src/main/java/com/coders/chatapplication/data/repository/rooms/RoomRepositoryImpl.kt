package com.coders.chatapplication.data.repository.rooms

import com.coders.chatapplication.data.db.room.RoomDao
import com.coders.chatapplication.data.db.room.RoomEntity
import com.coders.chatapplication.data.net.api.RoomsService
import com.coders.chatapplication.data.net.asDomain
import com.coders.chatapplication.domain.model.RoomModel
import com.coders.chatapplication.domain.repository.RoomRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomRepositoryImpl(
	private val roomsService: RoomsService,
	private val roomDao: RoomDao
) : RoomRepository {

	override suspend fun getRooms(): Flow<List<RoomModel>> {
		return roomDao.getRoomsWithUsers().map { list -> list.map { it.asDomain() } }
	}

	override suspend fun getRoomWithUsers(roomId: Long): Flow<RoomModel> {
		return roomDao.getRoomWithUsers(roomId).map { it.asDomain() }
	}

	override suspend fun updateRooms() {
		val rooms = roomsService.getMyRooms().map {
			RoomEntity(it.id!!, it.name)
		}
		roomDao.insert(*(rooms.toTypedArray()))
	}
}