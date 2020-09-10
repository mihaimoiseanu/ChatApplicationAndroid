package com.coders.chatapplication.presentation.ui.register

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.coders.chatapplication.domain.model.UserModel
import com.coders.chatapplication.domain.usecase.auth.RegisterUseCase
import com.coders.chatapplication.presentation.commons.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterViewModel @ViewModelInject constructor(
	private val registerUseCase: RegisterUseCase
) : BaseViewModel() {

	val registerSuccess = MutableLiveData<Boolean>()

	fun register(email: String, pass: String, firstName: String, lastName: String) {
		viewModelScope.launch(Dispatchers.IO) {
			val model = UserModel(
				email = email,
				pass = pass,
				firstName = firstName,
				lastName = lastName
			)
			registerUseCase(this, model) {
				it.either(::handleFailure, ::handleSuccess)
			}
		}
	}

	private fun handleSuccess(userModel: UserModel) {
		registerSuccess.postValue(true)
	}
}