package com.coders.chatapplication.presentation.ui.login

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.coders.chatapplication.domain.model.UserModel
import com.coders.chatapplication.domain.usecase.auth.LoginUseCase
import com.coders.chatapplication.presentation.commons.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel @ViewModelInject constructor(
	private val loginUseCase: LoginUseCase
) : BaseViewModel() {

	val loginSuccess = MutableLiveData<Boolean>()

	fun doLogin(email: String, password: String) {
		viewModelScope.launch {
			withContext(Dispatchers.IO) {
				loginUseCase(this, LoginUseCase.Params(email, password)) {
					it.either(::handleFailure, ::handleSuccess)
				}
			}
		}
	}

	private fun handleSuccess(user: UserModel) {
		loginSuccess.postValue(true)
	}

}