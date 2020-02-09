package com.coders.chatapplication.data.net.models

data class UserResponse(
	val id: Long? = null,
	val email: String?,
	val firstName: String? = null,
	val lastName: String? = null,
	val pass: String? = null
)