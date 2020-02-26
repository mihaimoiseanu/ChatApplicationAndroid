package com.coders.chatapplication.domain.di

import com.coders.chatapplication.domain.usecase.auth.CheckSessionUseCase
import com.coders.chatapplication.domain.usecase.auth.LoginUseCase
import com.coders.chatapplication.domain.usecase.auth.RegisterUseCase
import com.coders.chatapplication.domain.usecase.chat.GetRoomMessagesUseCase
import com.coders.chatapplication.domain.usecase.chat.SendMessageUseCase
import com.coders.chatapplication.domain.usecase.chat.SubscribeForEventsUseCase
import com.coders.chatapplication.domain.usecase.chat.UnSubscribeFromChannel
import com.coders.chatapplication.domain.usecase.chat.UpdateMessagesUseCase
import com.coders.chatapplication.domain.usecase.rooms.GetRoomWithUsersUseCase
import com.coders.chatapplication.domain.usecase.rooms.GetRoomsUseCase
import com.coders.chatapplication.domain.usecase.rooms.UpdatePublicRoomsUseCase
import com.coders.chatapplication.domain.usecase.users.DeleteFriendshipUseCase
import com.coders.chatapplication.domain.usecase.users.GetFriendRequestsUseCase
import com.coders.chatapplication.domain.usecase.users.GetFriendshipUseCase
import com.coders.chatapplication.domain.usecase.users.RequestFriendshipUseCase
import com.coders.chatapplication.domain.usecase.users.SearchUsersUseCase
import com.coders.chatapplication.domain.usecase.users.UpdateFriendshipUseCase
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

	single {
		GetFriendshipUseCase(get())
	}

	single {
		RequestFriendshipUseCase(get())
	}

	single {
		UpdateFriendshipUseCase(get())
	}

	single {
		DeleteFriendshipUseCase(get())
	}

	single {
		GetFriendRequestsUseCase(get())
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
	single { SubscribeForEventsUseCase(get()) }

	single { UnSubscribeFromChannel(get()) }

	single { SendMessageUseCase(get(), get()) }

	single {
		UpdateMessagesUseCase(get())
	}

	single { Gson() }
}