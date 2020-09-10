package com.coders.chatapplication.data.di

import com.coders.chatapplication.BuildConfig
import com.coders.chatapplication.data.net.api.AuthService
import com.coders.chatapplication.data.net.api.RoomsService
import com.coders.chatapplication.data.net.api.UserService
import com.coders.chatapplication.data.net.interceptors.AuthInterceptor
import com.coders.chatapplication.data.sharedprefs.SharedPrefs
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object NetworkModule {

    const val BASE_URL = "192.168.50.148:8080"

    @Provides
    @Singleton
    fun provideAuthInterceptor(sharedPrefs: SharedPrefs) = AuthInterceptor().apply {
        authToken = sharedPrefs.authToken
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.addInterceptor(authInterceptor)
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        }
        return builder.build()
    }

    @Provides
    @Singleton
    fun provideRetrofitClient(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("http://$BASE_URL/api/")
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthService(retrofit: Retrofit) = retrofit.create(AuthService::class.java)

    @Provides
    @Singleton
    fun provideRoomService(retrofit: Retrofit) = retrofit.create(RoomsService::class.java)

    @Provides
    @Singleton
    fun provideUserService(retrofit: Retrofit) = retrofit.create(UserService::class.java)
}