package com.coders.chatapplication.domain.model

data class MessageModel(
	val id:Long? = null,
	val message:String,
	val sender: Long,
	val sentAt: Long? = null,
	val roomId: Long
)