package com.coders.chatapplication.domain.model

data class UserModel(
	val id: Long? = null,
	val email: String? = null,
	val firstName: String? = null,
	val lastName: String? = null,
	val pass: String? = null
)