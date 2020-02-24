package com.coders.chatapplication.data.repository.users

import com.coders.chatapplication.data.net.api.UserService
import com.coders.chatapplication.data.net.asDomain
import com.coders.chatapplication.data.net.asResponse
import com.coders.chatapplication.domain.model.FriendshipModel
import com.coders.chatapplication.domain.model.FriendshipStatus
import com.coders.chatapplication.domain.model.UserModel
import com.coders.chatapplication.domain.repository.UserRepository

class UserRepositoryImpl(
	private val userService: UserService
) : UserRepository {

	override suspend fun searchUsers(searchTerm: String?): List<UserModel> {
		return userService.getUsers(searchTerm).map { it.asDomain() }
	}

	override suspend fun getFriendship(otherUserId: Long): FriendshipModel {
		return userService.getFriendship(otherUserId).asDomain()
	}

	override suspend fun requestFriendship(params: FriendshipModel): FriendshipModel {
		return userService.requestFriendship(params.asResponse()).asDomain()
	}

	override suspend fun updateFriendship(params: FriendshipModel): FriendshipModel {
		return userService.updateFriendship(params.asResponse()).asDomain()
	}

	override suspend fun deleteFriendship(params: Long) {
		userService.deleteFriendships(params)
	}

	override suspend fun getFriendships(status: FriendshipStatus): List<FriendshipModel> {
		return userService.getFriendships(FriendshipStatus.PENDING).map { it.asDomain() }
	}
}