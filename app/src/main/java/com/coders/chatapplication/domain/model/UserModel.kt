package com.coders.chatapplication.domain.model

data class UserModel(
	val id:Long?,
	val email:String? = null,
	val firstName:String? = null,
	val lastName:String? = null
)