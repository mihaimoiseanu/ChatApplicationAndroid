package com.coders.chatapplication.domain.model

data class MessageModel(
	val id:Long? = null,
	val message:String,
	val sender:UserModel,
	val sentAt:Long? = null
)