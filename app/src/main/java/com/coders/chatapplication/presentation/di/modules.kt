package com.coders.chatapplication.presentation.di

import com.coders.chatapplication.presentation.ui.chat.ChatViewModel
import com.coders.chatapplication.presentation.ui.login.LoginViewModel
import com.coders.chatapplication.presentation.ui.rooms.RoomsViewModel
import com.coders.chatapplication.presentation.ui.splash.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {

	viewModel {
		LoginViewModel(get())
	}

	viewModel {
		RoomsViewModel(get(), get(), get())
	}

	viewModel {
		ChatViewModel(
			get(), get(), get(), get(),
			get()
		)
	}

	viewModel {
		SplashViewModel(get())
	}
}
