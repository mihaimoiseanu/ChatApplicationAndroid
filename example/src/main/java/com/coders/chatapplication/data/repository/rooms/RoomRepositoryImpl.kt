package com.coders.chatapplication.data.repository.rooms

import com.coders.chatapplication.data.net.models.RoomResponse
import com.coders.chatapplication.data.repository.rooms.datasource.RemoteDataSource
import com.coders.chatapplication.domain.model.MessageModel
import com.coders.chatapplication.domain.model.RoomModel
import com.coders.chatapplication.domain.repository.RoomRepository

class RoomRepositoryImpl(
	private val remoteDataSource: RemoteDataSource
) : RoomRepository {
	override suspend fun createRoom(name: String, isPrivate: Boolean): RoomModel {
		val room = RoomResponse(
			name = name,
			isPrivate = isPrivate
		)
		return remoteDataSource.createRoom(room).asDomain()
	}

	override suspend fun getPublicRooms(): List<RoomModel> {
		return remoteDataSource.getPublicRooms().map { it.asDomain() }
	}

	override suspend fun getMyRooms(): List<RoomModel> {
		return remoteDataSource.getMyRooms().map { it.asDomain() }
	}

	override suspend fun getRoomMessages(roomId: Long): List<MessageModel> {
		return remoteDataSource.getRoomMessages(roomId).map { it.asDomain() }
	}
}