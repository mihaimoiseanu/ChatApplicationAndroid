package com.coders.chatapplication.presentation.ui.rooms

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.coders.chatapplication.commons.domain.usecase.NoParams
import com.coders.chatapplication.domain.model.RoomModel
import com.coders.chatapplication.domain.usecase.rooms.GetPublicRoomsUseCase
import com.coders.chatapplication.domain.usecase.rooms.GetRoomsUseCase
import com.coders.chatapplication.presentation.commons.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RoomsViewModel(
	private val getRoomsUseCase: GetRoomsUseCase,
	private val getPublicRoomsUseCase: GetPublicRoomsUseCase
): BaseViewModel() {

	val onRoomsReceived = MutableLiveData<List<RoomModel>>()

	fun getRooms(){
		viewModelScope.launch {
			withContext(Dispatchers.IO){
				getPublicRoomsUseCase(this, NoParams ){
					it.either(::handleFailure,::handlePublicRoomSuccess)
				}
			}
		}
	}

	private fun handlePublicRoomSuccess(rooms:List<RoomModel>){
		onRoomsReceived.postValue(rooms)
	}
}