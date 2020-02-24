package com.coders.chatapplication.presentation.di

import com.coders.chatapplication.presentation.ui.chat.ChatViewModel
import com.coders.chatapplication.presentation.ui.login.LoginViewModel
import com.coders.chatapplication.presentation.ui.profile.ProfileViewModel
import com.coders.chatapplication.presentation.ui.register.RegisterViewModel
import com.coders.chatapplication.presentation.ui.requests.FriendRequestViewModel
import com.coders.chatapplication.presentation.ui.rooms.RoomsViewModel
import com.coders.chatapplication.presentation.ui.searchfriends.SearchFriendsViewModel
import com.coders.chatapplication.presentation.ui.splash.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {

	viewModel {
		LoginViewModel(get())
	}

	viewModel {
		RegisterViewModel(get())
	}

	viewModel {
		RoomsViewModel(get(), get())
	}

	viewModel {
		ChatViewModel(
			get(), get(), get(), get(),
			get(), get()
		)
	}

	viewModel {
		SearchFriendsViewModel(get())
	}

	viewModel {
		FriendRequestViewModel(get(), get(), get())
	}

	viewModel {
		ProfileViewModel(get(), get(), get(), get(), get())
	}

	viewModel {
		SplashViewModel(get())
	}
}
