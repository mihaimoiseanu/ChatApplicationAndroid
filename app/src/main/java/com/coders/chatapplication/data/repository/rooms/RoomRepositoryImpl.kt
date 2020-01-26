package com.coders.chatapplication.data.repository.rooms

import com.coders.chatapplication.data.db.room.RoomDao
import com.coders.chatapplication.data.db.room.RoomEntity
import com.coders.chatapplication.data.net.api.RoomsService
import com.coders.chatapplication.data.net.models.RoomResponse
import com.coders.chatapplication.domain.model.RoomModel
import com.coders.chatapplication.domain.repository.RoomRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomRepositoryImpl(
	private val roomsService: RoomsService,
	private val roomDao: RoomDao
) : RoomRepository {
	override suspend fun createRoom(name: String, isPrivate: Boolean): RoomModel {
		val room = RoomResponse(
			name = name,
			isPrivate = isPrivate
		)
		return roomsService.createRoom(room).asDomain()
	}

	override suspend fun getPublicRooms(): Flow<List<RoomModel>> {
		return roomDao.getPublicRooms().map { list -> list.map { it.asDomain() } }
	}

	override suspend fun getMyRooms(): Flow<List<RoomModel>> {
		return roomDao.getPublicRooms().map { list -> list.map { it.asDomain() } }
	}

	override suspend fun updatePublicRooms() {
		val rooms = roomsService.getPublicRooms().map {
			RoomEntity(it.id!!, it.name, it.isPrivate)
		}
		roomDao.insert(*(rooms.toTypedArray()))
	}

	override suspend fun updateMyRoom() {
		val rooms = roomsService.getMyRooms().map {
			RoomEntity(it.id!!, it.name, it.isPrivate)
		}
		roomDao.insert(*(rooms.toTypedArray()))
	}
}