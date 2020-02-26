package com.coders.chatapplication.data.net.models

data class MessageResponse(
	val id: Long? = null,
	val message: String? = null,
	val senderId: Long? = null,
	val roomId: Long? = null,
	val sentAt: Long? = null
) : EventDTO