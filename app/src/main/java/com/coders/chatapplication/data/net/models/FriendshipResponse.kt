package com.coders.chatapplication.data.net.models

data class FriendshipResponse(
	val user: UserResponse,
	val status: Status
) {
	enum class Status {
		PENDING,
		ACCEPTED,
		BLOCKED
	}
}
