package com.coders.chatapplication.data.repository.friendship

import com.coders.chatapplication.data.db.friendships.FriendshipDao
import com.coders.chatapplication.data.db.friendships.FriendshipEntity
import com.coders.chatapplication.data.db.user.UserDao
import com.coders.chatapplication.data.net.api.UserService
import com.coders.chatapplication.data.net.asDomain
import com.coders.chatapplication.data.net.asEntity
import com.coders.chatapplication.domain.model.FriendshipModel
import com.coders.chatapplication.domain.model.FriendshipStatus
import com.coders.chatapplication.domain.repository.FriendshipRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull

class FriendshipRepositoryImpl(
	private val friendshipDao: FriendshipDao,
	private val userService: UserService,
	private val userDao: UserDao
) : FriendshipRepository {
	override suspend fun getFriendships(status: FriendshipStatus): Flow<List<FriendshipModel>> {
		return friendshipDao.getAllFriendships(status.ordinal)
			.map { list -> list.map { it.asDomain() } }
	}

	override suspend fun getFriendship(otherUserId: Long): Flow<FriendshipModel> {
		//not the bet solution...
		val friendship = userService.getFriendship(otherUserId)
		userDao.insert(friendship.user.asEntity())
		friendshipDao.insertFriendships(friendship.asEntity())
		return friendshipDao.getFriendshipWithUser(otherUserId).mapNotNull { it?.asDomain() }
	}

	override suspend fun insertFriendship(friendshipModel: FriendshipModel) {
		val user = friendshipModel.userModel.asEntity()
		userDao.insert(user)
		friendshipDao.insertFriendships(
			FriendshipEntity(
				lastUserModifyId = friendshipModel.lastUserActioned!!,
				status = friendshipModel.friendshipStatus,
				userId = user.userId
			)
		)
	}

	override suspend fun updateFriendship(friendshipModel: FriendshipModel) {
		friendshipDao.insertFriendships(
			FriendshipEntity(
				lastUserModifyId = friendshipModel.lastUserActioned!!,
				status = friendshipModel.friendshipStatus,
				userId = friendshipModel.userModel.id!!
			)
		)
	}

	override suspend fun deleteFriendship(friendshipModel: FriendshipModel) {
		friendshipDao.getFriendship(friendshipModel.userModel.id!!)?.let {
			friendshipDao.deleteFriendship(it)
		}
	}

	override suspend fun update() {
		val response = userService.getFriendships()
		response.forEach {
			userDao.insert(it.user.asEntity())
		}
		friendshipDao.insertFriendships(*(response.map { it.asEntity() }.toTypedArray()))
	}
}