package com.coders.chatapplication.domain.repository

import com.coders.chatapplication.domain.model.FriendshipModel
import com.coders.chatapplication.domain.model.FriendshipStatus
import com.coders.chatapplication.domain.model.UserModel

interface UserRepository {

	suspend fun searchUsers(searchTerm: String?): List<UserModel>

	suspend fun getFriendship(otherUserId: Long): FriendshipModel

	suspend fun requestFriendship(params: FriendshipModel): FriendshipModel

	suspend fun updateFriendship(params: FriendshipModel): FriendshipModel

	suspend fun deleteFriendship(params: Long)

	suspend fun getFriendships(status: FriendshipStatus): List<FriendshipModel>
}