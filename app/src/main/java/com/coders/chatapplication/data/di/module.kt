package com.coders.chatapplication.data.di

import androidx.room.Room
import com.coders.chatapplication.BuildConfig
import com.coders.chatapplication.data.db.ChatDatabase
import com.coders.chatapplication.data.net.api.AuthService
import com.coders.chatapplication.data.net.api.RoomsService
import com.coders.chatapplication.data.net.interceptors.AuthInterceptor
import com.coders.chatapplication.data.net.socket.ChatManagerImpl
import com.coders.chatapplication.data.repository.auth.AuthRepositoryImpl
import com.coders.chatapplication.data.repository.auth.datasource.RemoteDataSource
import com.coders.chatapplication.data.repository.chat.ChatRepositoryImpl
import com.coders.chatapplication.data.repository.rooms.RoomRepositoryImpl
import com.coders.chatapplication.data.sharedprefs.SharedPrefs
import com.coders.chatapplication.domain.repository.AuthRepository
import com.coders.chatapplication.domain.repository.ChatManager
import com.coders.chatapplication.domain.repository.ChatRepository
import com.coders.chatapplication.domain.repository.RoomRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {
	single<AuthRepository> {
		AuthRepositoryImpl(
			RemoteDataSource(get()),
			get(),
			get(),
			get()
		)
	}

	single<RoomRepository> {
		RoomRepositoryImpl(
			get(),
			get()
		)
	}

	single<ChatRepository> {
		ChatRepositoryImpl(get(), get())
	}

	single {
		SharedPrefs(androidApplication())
	}
}

val dbModule = module {
	single {
		Room
			.databaseBuilder(androidApplication(), ChatDatabase::class.java, "chat-db")
			.build()
	}

	single {
		get<ChatDatabase>().userDao()
	}

	single {
		get<ChatDatabase>().messageDao()
	}

	single {
		get<ChatDatabase>().roomDao()
	}

}

val networkingModule = module {

	single {
		AuthInterceptor().apply {
			authToken = get<SharedPrefs>().authToken
		}
	}

	single {
		val builder = OkHttpClient.Builder()
		builder.addInterceptor(get<AuthInterceptor>())
		if (BuildConfig.DEBUG) {
			builder.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
		}
		builder.build()
	}

	single {
		Retrofit.Builder()
			.client(get())
			.addConverterFactory(GsonConverterFactory.create())
			.baseUrl("http://192.168.0.118:8080/api/")
			.build()
	}

	single {
		get<Retrofit>().create(AuthService::class.java)
	}

	single {
		get<Retrofit>().create(RoomsService::class.java)
	}

	single<ChatManager> {
		ChatManagerImpl(get(), get(), get())
	}

}