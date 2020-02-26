package com.coders.chatapplication.data.repository.rooms

import com.coders.chatapplication.data.db.room.RoomDao
import com.coders.chatapplication.data.db.room.RoomEntity
import com.coders.chatapplication.data.db.room.UserRoomCrossRef
import com.coders.chatapplication.data.db.room.UserRoomDao
import com.coders.chatapplication.data.db.user.UserDao
import com.coders.chatapplication.data.db.user.UserEntity
import com.coders.chatapplication.data.net.api.RoomsService
import com.coders.chatapplication.data.net.asDomain
import com.coders.chatapplication.data.net.asEntity
import com.coders.chatapplication.domain.model.RoomModel
import com.coders.chatapplication.domain.repository.RoomRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomRepositoryImpl(
	private val roomsService: RoomsService,
	private val roomDao: RoomDao,
	private val userRoomDao: UserRoomDao,
	private val userDao: UserDao
) : RoomRepository {

	override suspend fun getRooms(): Flow<List<RoomModel>> {
		return roomDao.getRoomsWithUsers().map { list -> list.map { it.asDomain() } }
	}

	override suspend fun getRoomWithUsers(roomId: Long): Flow<RoomModel> {
		return roomDao.getRoomWithUsers(roomId).map { it.asDomain() }
	}

	override suspend fun updateRooms() {
		val users = mutableListOf<UserEntity>()
		val userRoomEntity = mutableListOf<UserRoomCrossRef>()
		val rooms = roomsService.getMyRooms().map { room ->
			room.users?.map { user ->
				userRoomEntity.add(UserRoomCrossRef(room.id!!, user.id!!))
				user.asEntity()
			}?.let { list ->
				users.addAll(list)
			}
			RoomEntity(room.id!!, room.name)
		}
		val roomsIds = rooms.map { it.roomId }
		userRoomDao.delete(roomsIds)
		userRoomDao.insert(*(userRoomEntity).toTypedArray())
		userDao.insert(*(users).toTypedArray())
		roomDao.deleteRooms(roomsIds)
		roomDao.insert(*(rooms.toTypedArray()))
	}
}