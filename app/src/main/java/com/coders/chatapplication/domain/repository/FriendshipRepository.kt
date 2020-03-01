package com.coders.chatapplication.domain.repository

import com.coders.chatapplication.domain.model.FriendshipModel
import com.coders.chatapplication.domain.model.FriendshipStatus
import kotlinx.coroutines.flow.Flow

interface FriendshipRepository {

	suspend fun getFriendships(status: FriendshipStatus): Flow<List<FriendshipModel>>

	suspend fun getFriendship(otherUserId: Long): Flow<FriendshipModel>

	suspend fun insertFriendship(friendshipModel: FriendshipModel)

	suspend fun updateFriendship(friendshipModel: FriendshipModel)

	suspend fun deleteFriendship(friendshipModel: FriendshipModel)

	suspend fun update()

}