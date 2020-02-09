package com.coders.chatapplication.domain.repository

import com.coders.chatapplication.domain.model.UserModel

interface AuthRepository {

	suspend fun login(email: String, password: String): UserModel

	suspend fun getPrincipal(): UserModel

	suspend fun register(params: UserModel): UserModel
}