package com.coders.chatapplication.presentation.commons

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.coders.chatapplication.commons.domain.exception.Failure


open class BaseViewModel : ViewModel() {
	var failure = MutableLiveData<Failure>()

	protected fun handleFailure(failure: Failure) {
		this.failure.postValue(failure)
	}
}