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
import com.coders.chatapplication.domain.repository.MessageRepository
import com.coders.chatapplication.domain.repository.RoomRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomRepositoryImpl(
	private val roomsService: RoomsService,
	private val roomDao: RoomDao,
	private val userRoomDao: UserRoomDao,
	private val userDao: UserDao,
	private val messageRepository: MessageRepository
) : RoomRepository {

	override suspend fun getRooms(): Flow<List<RoomModel>> {
		return roomDao.getRoomWithMessage().map { list -> list.map { it.asDomain() } }
	}

	override suspend fun getRoomWithUsers(roomId: Long): Flow<RoomModel> {
		return roomDao.getRoomWithUsers(roomId).map { it.asDomain() }
	}

	override suspend fun updateRoomLastMessage(roomId: Long, messageId: Long) {
		roomDao.updateRoomLastMessage(roomId, messageId)
	}

	override suspend fun insertRoom(model: RoomModel) {
		val userRoomEntity = mutableListOf<UserRoomCrossRef>()
		val users = mutableListOf<UserEntity>()
		model.users?.map { user ->
			userRoomEntity.add(UserRoomCrossRef(model.id!!, user.id!!))
			user.asEntity()
		}?.let { list ->
			users.addAll(list)
		}
		userDao.insert(*(users.toTypedArray()))
		userRoomDao.insert(*(userRoomEntity).toTypedArray())
		roomDao.insert(RoomEntity(model.id, model.name))
		messageRepository.updateMessages(model.id!!)
	}

	override suspend fun updateRoom(model: RoomModel) {
		val roomEntity = model.asEntity()
		val userRoomEntity = mutableListOf<UserRoomCrossRef>()
		val users = mutableListOf<UserEntity>()
		model.users?.map { user ->
			userRoomEntity.add(UserRoomCrossRef(model.id!!, user.id!!))
			user.asEntity()
		}?.let { list ->
			users.addAll(list)
		}
		userRoomDao.delete(listOf(model.id!!))
		userDao.insert(*(users.toTypedArray()))
		userRoomDao.insert(*(userRoomEntity).toTypedArray())
		roomDao.insert(roomEntity)
		messageRepository.updateMessages(model.id)
	}

	override suspend fun deleteRoom(room: RoomModel) {
		userRoomDao.delete(room.id!!)
		messageRepository.deleteMessages(room.id)
		roomDao.deleteRoom(room.id)
	}

	override suspend fun updateRooms() {
		val users = mutableListOf<UserEntity>()
		val userRoomEntity = mutableListOf<UserRoomCrossRef>()
		val rooms = roomsService.getRooms().map { room ->
			room.users?.map { user ->
				userRoomEntity.add(UserRoomCrossRef(room.id!!, user.id!!))
				user.asEntity()
			}?.let { list ->
				users.addAll(list)
			}
			RoomEntity(room.id!!, room.name, lastMessageId = room.lastMessageId)
		}
		val roomsIds = rooms.mapNotNull { it.roomId }
		userRoomDao.delete(roomsIds)
		userRoomDao.insert(*(userRoomEntity).toTypedArray())
		userDao.insert(*(users).toTypedArray())
		roomDao.deleteRooms(roomsIds)
		roomDao.insert(*(rooms.toTypedArray()))
	}
}