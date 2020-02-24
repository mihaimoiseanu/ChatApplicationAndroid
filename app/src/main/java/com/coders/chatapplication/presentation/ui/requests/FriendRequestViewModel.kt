package com.coders.chatapplication.presentation.ui.requests

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.coders.chatapplication.commons.domain.usecase.NoParams
import com.coders.chatapplication.domain.model.FriendshipModel
import com.coders.chatapplication.domain.usecase.users.DeleteFriendshipUseCase
import com.coders.chatapplication.domain.usecase.users.GetFriendRequestsUseCase
import com.coders.chatapplication.domain.usecase.users.UpdateFriendshipUseCase
import com.coders.chatapplication.presentation.commons.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FriendRequestViewModel(
	private val getFriendRequestsUseCase: GetFriendRequestsUseCase,
	private val updateFriendshipUseCase: UpdateFriendshipUseCase,
	private val deleteFriendshipUseCase: DeleteFriendshipUseCase
) : BaseViewModel() {

	val requests = MutableLiveData<List<FriendshipModel>>()

	fun loadRequests() {
		viewModelScope.launch(Dispatchers.IO) {
			getFriendRequestsUseCase(this, NoParams) {
				it.either(::handleFailure, ::onGetRequestSuccess)
			}
		}
	}

	private fun onGetRequestSuccess(list: List<FriendshipModel>) {
		requests.postValue(list)
	}

	fun updateFriendship(friendshipToUpdate: FriendshipModel) {
		viewModelScope.launch(Dispatchers.IO) {
			updateFriendshipUseCase(this, friendshipToUpdate) {
				it.either(::handleFailure, ::handleGetFriendshipSuccess)
			}
		}
	}

	fun deleteFriendship(otherUserId: Long) {
		viewModelScope.launch(Dispatchers.IO) {
			deleteFriendshipUseCase(this, otherUserId) {
				it.either(::handleFailure, ::handleDeleteSuccess)
			}
		}
	}

	private fun handleGetFriendshipSuccess(model: FriendshipModel) {
		//reload data
		loadRequests()
	}

	private fun handleDeleteSuccess(unit: Unit) {
		//reload data
		loadRequests()
	}

}