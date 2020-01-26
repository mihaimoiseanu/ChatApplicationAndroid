package com.coders.chatapplication.domain.model

data class RoomModel(
	val id:Long?,
	val name:String,
	val isPrivate:Boolean,
	val createdBy: Long?,
	val users: List<Long>?
)