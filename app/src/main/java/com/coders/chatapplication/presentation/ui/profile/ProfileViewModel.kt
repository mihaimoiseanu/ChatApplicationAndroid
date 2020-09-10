package com.coders.chatapplication.presentation.ui.profile

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.coders.chatapplication.data.sharedprefs.SharedPrefs
import com.coders.chatapplication.domain.model.FriendshipModel
import com.coders.chatapplication.domain.model.FriendshipStatus
import com.coders.chatapplication.domain.usecase.users.DeleteFriendshipUseCase
import com.coders.chatapplication.domain.usecase.users.GetFriendshipUseCase
import com.coders.chatapplication.domain.usecase.users.RequestFriendshipUseCase
import com.coders.chatapplication.domain.usecase.users.UpdateFriendshipUseCase
import com.coders.chatapplication.presentation.commons.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ProfileViewModel @ViewModelInject constructor(
	private val getFriendshipUseCase: GetFriendshipUseCase,
	private val requestFriendshipUseCase: RequestFriendshipUseCase,
	private val updateFriendshipUseCase: UpdateFriendshipUseCase,
	private val deleteFriendshipUseCase: DeleteFriendshipUseCase,
	sharedPrefs: SharedPrefs
) : BaseViewModel() {

	val thisUserId = sharedPrefs.userId
	val friendship = MutableLiveData<FriendshipModel>()

	fun getFriendship(otherUserId: Long) {
		viewModelScope.launch(Dispatchers.IO) {
			getFriendshipUseCase(this, otherUserId) {
				it.either(::handleFailure, ::handleGetFriendshipSuccess)
			}
		}
	}

	private fun handleGetFriendshipSuccess(result: Flow<FriendshipModel>) {
		result.onEach {
			friendship.postValue(it)
		}.launchIn(viewModelScope)
	}

	fun sendAction() {
		val friendship = friendship.value ?: return
		when (friendship.friendshipStatus) {
			FriendshipStatus.PENDING -> {
				if (thisUserId == friendship.lastUserActioned) {
					deleteFriendship(friendship)
				} else {
					val friendshipToUpdate =
						FriendshipModel(FriendshipStatus.ACCEPTED, friendship.userModel)
					updateFriendship(friendshipToUpdate)
				}
			}
			FriendshipStatus.ACCEPTED -> deleteFriendship(friendship)
			FriendshipStatus.BLOCKED -> {
				val friendshipToUpdate =
					FriendshipModel(FriendshipStatus.ACCEPTED, friendship.userModel)
				updateFriendship(friendshipToUpdate)
			}
			FriendshipStatus.NONE -> {
				requestFriendship(friendship)
			}
		}
	}

	private fun requestFriendship(friendship: FriendshipModel) {
		viewModelScope.launch(Dispatchers.IO) {
			requestFriendshipUseCase(this, friendship) {
				it.either(::handleFailure)
			}
		}
	}

	private fun updateFriendship(friendshipToUpdate: FriendshipModel) {
		viewModelScope.launch(Dispatchers.IO) {
			updateFriendshipUseCase(this, friendshipToUpdate) {
				it.either(::handleFailure)
			}
		}
	}

	private fun deleteFriendship(otherUserId: FriendshipModel) {
		viewModelScope.launch(Dispatchers.IO) {
			deleteFriendshipUseCase(this, otherUserId) {
				it.either(::handleFailure, ::handleDeleteSuccess)
			}
		}
	}

	private fun handleDeleteSuccess(unit: Unit) {
		val fr = friendship.value ?: return
		friendship.postValue(
			FriendshipModel(FriendshipStatus.NONE, fr.userModel)
		)
	}

	fun blockUser() {
		val fr = friendship.value ?: return
		friendship.postValue(
			FriendshipModel(FriendshipStatus.BLOCKED, fr.userModel)
		)
	}
}