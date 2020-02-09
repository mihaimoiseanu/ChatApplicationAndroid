package com.coders.chatapplication.presentation.ui.splash

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.coders.chatapplication.commons.domain.usecase.NoParams
import com.coders.chatapplication.domain.model.UserModel
import com.coders.chatapplication.domain.usecase.auth.CheckSessionUseCase
import com.coders.chatapplication.presentation.commons.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel(
	private val checkSessionUseCase: CheckSessionUseCase
) : BaseViewModel() {

	val onSuccess = MutableLiveData<Boolean>()

	fun checkSession() {
		viewModelScope.launch(Dispatchers.IO) {
			checkSessionUseCase(viewModelScope, NoParams) {
				it.either(::handleFailure, ::handleSuccess)
			}
		}
	}

	private fun handleSuccess(userModel: UserModel) {
		viewModelScope.launch {
			delay(2_000)
			onSuccess.postValue(true)
		}
	}

}