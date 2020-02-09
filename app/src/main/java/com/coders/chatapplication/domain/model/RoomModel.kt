package com.coders.chatapplication.domain.model

data class RoomModel(
	val id: Long?,
	val name: String? = null,
	val users: List<UserModel>?
)