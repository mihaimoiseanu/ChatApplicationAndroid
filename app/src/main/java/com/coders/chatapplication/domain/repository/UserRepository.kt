package com.coders.chatapplication.domain.repository

import com.coders.chatapplication.domain.model.UserModel

interface UserRepository {

	suspend fun searchUsers(searchTerm: String?): List<UserModel>
}