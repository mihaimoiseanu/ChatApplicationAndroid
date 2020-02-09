package com.coders.chatapplication.data.net.api

import com.coders.chatapplication.data.net.models.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthService {


	@GET("users/principal")
	suspend fun getPrincipal(@Header("Authorization") authorization: String): Response<UserResponse>

	@GET("users/principal")
	suspend fun getPrincipal(): UserResponse

	@POST("users/signup")
	suspend fun register(@Body userResponse: UserResponse): UserResponse

}