package com.coders.chatapplication.domain.di

import com.coders.chatapplication.domain.usecase.auth.CheckSessionUseCase
import com.coders.chatapplication.domain.usecase.auth.LoginUseCase
import com.coders.chatapplication.domain.usecase.auth.RegisterUseCase
import com.coders.chatapplication.domain.usecase.chat.GetRoomMessagesUseCase
import com.coders.chatapplication.domain.usecase.chat.SendMessageUseCase
import com.coders.chatapplication.domain.usecase.chat.SubscribeToRoomUseCase
import com.coders.chatapplication.domain.usecase.chat.UnSubscribeFromChannel
import com.coders.chatapplication.domain.usecase.chat.UpdateMessagesUseCase
import com.coders.chatapplication.domain.usecase.rooms.GetRoomWithUsersUseCase
import com.coders.chatapplication.domain.usecase.rooms.GetRoomsUseCase
import com.coders.chatapplication.domain.usecase.rooms.UpdatePublicRoomsUseCase
import com.coders.chatapplication.domain.usecase.users.SearchUsersUseCase
import com.google.gson.Gson
import org.koin.dsl.module

val domainModule = module {

	//AUTH
	single {
		LoginUseCase(get())
	}
	single {
		CheckSessionUseCase(get())
	}

	single {
		RegisterUseCase(get())
	}

	//USERS
	single {
		SearchUsersUseCase(get())
	}

	//ROOMS
	single {
		GetRoomsUseCase(get())
	}
	single {
		UpdatePublicRoomsUseCase(get())
	}
	single {
		GetRoomWithUsersUseCase(get())
	}

	//CHAT
	single {
		GetRoomMessagesUseCase(get())
	}
	single { SubscribeToRoomUseCase(get()) }

	single { UnSubscribeFromChannel(get()) }

	single { SendMessageUseCase(get(), get()) }

	single {
		UpdateMessagesUseCase(get())
	}

	single { Gson() }
}