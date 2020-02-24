package com.coders.chatapplication.presentation.ui.rooms

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.coders.chatapplication.commons.domain.usecase.NoParams
import com.coders.chatapplication.domain.model.RoomModel
import com.coders.chatapplication.domain.usecase.rooms.GetRoomsUseCase
import com.coders.chatapplication.domain.usecase.rooms.UpdatePublicRoomsUseCase
import com.coders.chatapplication.presentation.commons.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RoomsViewModel(
	private val getRoomWithUsersUseCase: GetRoomsUseCase,
	private val updatePublicRoomsUseCase: UpdatePublicRoomsUseCase
) : BaseViewModel() {

	val onRoomsReceived = MutableLiveData<List<RoomModel>>()
	val onUpdateRoomSuccess = MutableLiveData<Boolean>()

	fun getRooms() {
		viewModelScope.launch(Dispatchers.IO) {
			getRoomWithUsersUseCase(this, NoParams) {
				it.either(::handleFailure, ::handlePublicRoomSuccess)
			}
		}
	}

	fun updateRooms() {
		viewModelScope.launch {
			withContext(Dispatchers.IO) {
				updatePublicRoomsUseCase(this, NoParams) {
					it.either(::handleFailure, ::handleUpdateSuccess)
				}
			}
		}
	}

	private fun handlePublicRoomSuccess(rooms: Flow<List<RoomModel>>) {
		rooms.onEach {
			onRoomsReceived.postValue(it)
		}.launchIn(viewModelScope)
	}

	private fun handleUpdateSuccess(noParam: Unit) {
		onUpdateRoomSuccess.postValue(true)
	}
}