package com.coders.chatapplication.domain.di

import com.coders.chatapplication.domain.usecase.auth.CheckSessionUseCase
import com.coders.chatapplication.domain.usecase.auth.LoginUseCase
import com.coders.chatapplication.domain.usecase.chat.GetRoomMessagesUseCase
import com.coders.chatapplication.domain.usecase.chat.SendMessageUseCase
import com.coders.chatapplication.domain.usecase.chat.SubscribeToRoomUseCase
import com.coders.chatapplication.domain.usecase.chat.UnSubscribeFromChannel
import com.coders.chatapplication.domain.usecase.rooms.CreateRoomUseCase
import com.coders.chatapplication.domain.usecase.rooms.GetPublicRoomsUseCase
import com.coders.chatapplication.domain.usecase.rooms.GetRoomsUseCase
import com.google.gson.Gson
import org.koin.dsl.module

val domainModule = module {

	single {
		LoginUseCase(get())
	}
	single {
		CreateRoomUseCase(get())
	}
	single {
		GetPublicRoomsUseCase(get())
	}
	single {
		GetRoomsUseCase(get())
	}

	single {
		GetRoomMessagesUseCase(get())
	}

	single { SubscribeToRoomUseCase(get()) }

	single { UnSubscribeFromChannel(get()) }

	single { SendMessageUseCase(get(), get()) }

	single {
		CheckSessionUseCase(get())
	}

	single { Gson() }
}