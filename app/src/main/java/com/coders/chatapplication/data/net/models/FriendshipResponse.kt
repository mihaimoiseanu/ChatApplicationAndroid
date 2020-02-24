package com.coders.chatapplication.data.net.models

import com.coders.chatapplication.domain.model.FriendshipStatus

data class FriendshipResponse(
	val user: UserResponse,
	val status: FriendshipStatus,
	val lastUserActioned: Long? = null
)
