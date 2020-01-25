package com.coders.chatapplication.data.repository.auth.datasource

import com.coders.chatapplication.data.net.api.AuthService
import com.coders.chatapplication.data.net.models.UserResponse
import okhttp3.Credentials
import retrofit2.Response

class RemoteDataSource(
	private val authService: AuthService
) {

	suspend fun login(email: String, password: String): Response<UserResponse> {
		val authorization = Credentials.basic(email, password)
		return authService.getPrincipal(authorization)
	}

	suspend fun getPrincipal(): UserResponse = authService.getPrincipal()
}