package com.coders.chatapplication.data.net.models

data class RoomResponse(
	val id: Long? = null,
	val name: String? = null,
	val users: List<UserResponse>? = null,
	val lastMessageId: Long
) : EventDTO