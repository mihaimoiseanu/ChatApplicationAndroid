package com.coders.chatapplication.presentation.ui.searchfriends

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.coders.chatapplication.domain.model.UserModel
import com.coders.chatapplication.domain.usecase.users.SearchUsersUseCase
import com.coders.chatapplication.presentation.commons.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchFriendsViewModel(
	private val searchUsersUseCase: SearchUsersUseCase
) : BaseViewModel() {

	val searchedUsers = MutableLiveData<List<UserModel>>()

	fun searchUsers(queryString: String) {
		viewModelScope.launch(Dispatchers.IO) {
			searchUsersUseCase(this, queryString) {
				it.either(::handleFailure, ::handleSuccess)
			}
		}
	}

	private fun handleSuccess(users: List<UserModel>) {
		searchedUsers.postValue(users)
	}
}