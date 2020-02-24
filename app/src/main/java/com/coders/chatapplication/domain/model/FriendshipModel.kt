package com.coders.chatapplication.domain.model

data class FriendshipModel(
	val friendshipStatus: FriendshipStatus,
	val userModel: UserModel,
	val lastUserActioned: Long? = null
)