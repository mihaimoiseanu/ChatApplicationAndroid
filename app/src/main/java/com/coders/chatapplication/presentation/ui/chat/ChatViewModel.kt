package com.coders.chatapplication.presentation.ui.chat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.coders.chatapplication.domain.model.MessageModel
import com.coders.chatapplication.domain.model.RoomModel
import com.coders.chatapplication.domain.model.UserModel
import com.coders.chatapplication.domain.usecase.chat.GetRoomMessagesUseCase
import com.coders.chatapplication.domain.usecase.chat.SendMessageUseCase
import com.coders.chatapplication.domain.usecase.chat.UpdateMessagesUseCase
import com.coders.chatapplication.domain.usecase.rooms.GetRoomWithUsersUseCase
import com.coders.chatapplication.presentation.commons.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatViewModel(
	private val getRoomMessagesUseCase: GetRoomMessagesUseCase,
	private val updateMessagesUseCase: UpdateMessagesUseCase,
	private val sendMessageUseCase: SendMessageUseCase,
	private val getRoomWithUsersUseCase: GetRoomWithUsersUseCase
) : BaseViewModel() {

	var roomId: Long = -1

	var roomMessages = MutableLiveData<List<MessageModel>>()
	var users = MutableLiveData<List<UserModel>>()

	fun getRoomMessages() {
		viewModelScope.launch(Dispatchers.IO) {
			getRoomMessagesUseCase(this, roomId) {
				it.either(::handleFailure, ::handleGetMessages)
			}
		}
	}

	fun updateMessages() {
		viewModelScope.launch(Dispatchers.IO) {
			updateMessagesUseCase(viewModelScope, roomId) {
				it.either(::handleFailure)
			}
		}
	}

	fun getRoomWithUsers() {
		viewModelScope.launch {
			withContext(Dispatchers.IO) {
				getRoomWithUsersUseCase(viewModelScope, roomId) {
					it.either(::handleFailure, ::handleGetRoomWithUser)
				}
			}
		}

	}

	fun sendMessage(message: String) {
		viewModelScope.launch {
			withContext(Dispatchers.IO) {
				sendMessageUseCase(viewModelScope, SendMessageUseCase.Params(roomId, message)) {
					it.either(::handleFailure)
				}
			}
		}
	}

	private fun handleGetRoomWithUser(response: Flow<RoomModel>) {
		response
			.map { it.users }
			.onEach {
				users.postValue(it)
				getRoomMessages()
			}
			.launchIn(viewModelScope)
	}

	private fun handleGetMessages(messages: Flow<List<MessageModel>>) {
		messages.onEach {
			roomMessages.postValue(it)
		}.launchIn(viewModelScope)
	}

}