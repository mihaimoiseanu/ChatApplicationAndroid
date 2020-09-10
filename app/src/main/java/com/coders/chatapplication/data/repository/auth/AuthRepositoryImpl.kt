package com.coders.chatapplication.data.repository.auth

import com.coders.chatapplication.data.net.api.AuthService
import com.coders.chatapplication.data.net.asDomain
import com.coders.chatapplication.data.net.asResponse
import com.coders.chatapplication.data.net.interceptors.AuthInterceptor
import com.coders.chatapplication.data.sharedprefs.SharedPrefs
import com.coders.chatapplication.domain.model.UserModel
import com.coders.chatapplication.domain.repository.AuthRepository
import com.coders.chatapplication.domain.repository.ChatManager
import okhttp3.Credentials

class AuthRepositoryImpl(
    private val authService: AuthService,
    private val authInterceptor: AuthInterceptor,
    private val chatManager: ChatManager,
    private val sharedPrefs: SharedPrefs
) : AuthRepository {
    override suspend fun login(email: String, password: String): UserModel {
        val auth = Credentials.basic(email, password)
        val response = authService.getPrincipal(auth)
        val authorization = response.headers()["Authorization"]
        authInterceptor.authToken = authorization
        if (response.code() < 400) {
            chatManager.connect()
        }
        sharedPrefs.authToken = authorization ?: ""
        sharedPrefs.userId = response.body()?.id ?: -1
        sharedPrefs.userEmail = response.body()?.email ?: ""
        return response.body()?.asDomain()!!
    }

    override suspend fun getPrincipal(): UserModel {
        val response = authService.getPrincipal().asDomain()
        chatManager.connect()
        return response
    }

    override suspend fun register(params: UserModel): UserModel {
        return authService.register(params.asResponse()).asDomain()
    }
}