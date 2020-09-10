package com.coders.chatapplication.presentation.ui.requests

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.coders.chatapplication.commons.domain.usecase.NoParams
import com.coders.chatapplication.domain.model.FriendshipModel
import com.coders.chatapplication.domain.model.FriendshipStatus
import com.coders.chatapplication.domain.usecase.users.DeleteFriendshipUseCase
import com.coders.chatapplication.domain.usecase.users.GetFriendRequestsUseCase
import com.coders.chatapplication.domain.usecase.users.UpdateFriendshipDBUseCase
import com.coders.chatapplication.domain.usecase.users.UpdateFriendshipUseCase
import com.coders.chatapplication.presentation.commons.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class FriendRequestViewModel @ViewModelInject constructor(
	private val updateFriendshipDBUseCase: UpdateFriendshipDBUseCase,
	private val getFriendRequestsUseCase: GetFriendRequestsUseCase,
	private val updateFriendshipUseCase: UpdateFriendshipUseCase,
	private val deleteFriendshipUseCase: DeleteFriendshipUseCase
) : BaseViewModel() {

	val requests = MutableLiveData<List<FriendshipModel>>()

	fun loadRequests() {
		viewModelScope.launch(Dispatchers.IO) {
			getFriendRequestsUseCase(this, FriendshipStatus.PENDING) {
				it.either(::handleFailure, ::onGetRequestSuccess)
			}
		}
	}

	fun updateFriendships() {
		viewModelScope.launch(Dispatchers.IO) {
			updateFriendshipDBUseCase(this, NoParams) {
				it.either(::handleFailure)
			}
		}
	}

	private fun onGetRequestSuccess(list: Flow<List<FriendshipModel>>) {
		list.onEach {
			requests.postValue(it)
		}.launchIn(viewModelScope)
	}

	fun updateFriendship(friendshipToUpdate: FriendshipModel) {
		viewModelScope.launch(Dispatchers.IO) {
			updateFriendshipUseCase(this, friendshipToUpdate) {
				it.either(::handleFailure)
			}
		}
	}

	fun deleteFriendship(otherUserId: FriendshipModel) {
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