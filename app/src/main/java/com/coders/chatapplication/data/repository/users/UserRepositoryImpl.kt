package com.coders.chatapplication.data.repository.users

import com.coders.chatapplication.data.net.api.UserService
import com.coders.chatapplication.data.net.asDomain
import com.coders.chatapplication.domain.model.UserModel
import com.coders.chatapplication.domain.repository.UserRepository

class UserRepositoryImpl(
	private val userService: UserService
) : UserRepository {

	override suspend fun searchUsers(searchTerm: String?): List<UserModel> {
		return userService.getUsers(searchTerm).map { it.asDomain() }
	}
}