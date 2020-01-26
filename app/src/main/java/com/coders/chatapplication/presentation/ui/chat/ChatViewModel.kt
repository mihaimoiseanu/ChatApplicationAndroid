package com.coders.chatapplication.presentation.ui.chat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.coders.chatapplication.domain.model.MessageModel
import com.coders.chatapplication.domain.usecase.chat.GetRoomMessagesUseCase
import com.coders.chatapplication.domain.usecase.chat.SendMessageUseCase
import com.coders.chatapplication.domain.usecase.chat.SubscribeToRoomUseCase
import com.coders.chatapplication.domain.usecase.chat.UnSubscribeFromChannel
import com.coders.chatapplication.domain.usecase.chat.UpdateMessagesUseCase
import com.coders.chatapplication.presentation.commons.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatViewModel(
	private val getRoomMessagesUseCase: GetRoomMessagesUseCase,
	private val subscribeToRoomUseCase: SubscribeToRoomUseCase,
	private val unSubscribeFromChannel: UnSubscribeFromChannel,
	private val updateMessagesUseCase: UpdateMessagesUseCase,
	private val sendMessageUseCase: SendMessageUseCase
) : BaseViewModel() {

	var roomId: Long = -1

	var roomMessages = MutableLiveData<List<MessageModel>>()

	fun getRoomMessages() {
		viewModelScope.launch {
			withContext(Dispatchers.IO) {
				getRoomMessagesUseCase(this, roomId) {
					it.either(::handleFailure, ::handleGetMessages)
				}
			}
		}
	}

	fun updateMessages() {
		updateMessagesUseCase(viewModelScope, roomId) {
			it.either(::handleFailure)
		}
	}

	fun subscribeToRoom() {
		subscribeToRoomUseCase(viewModelScope, roomId) {
			it.either(::handleFailure)
		}
	}

	fun sendMessage(message: String) {
		sendMessageUseCase(viewModelScope, SendMessageUseCase.Params(roomId, message)) {
			it.either(::handleFailure)
		}
	}

	fun unsubscribeFromRoom() {
		unSubscribeFromChannel(viewModelScope, roomId) {
			it.either(::handleFailure)
		}
	}

	private fun handleGetMessages(messages: Flow<List<MessageModel>>) {
		messages.onEach {
			roomMessages.postValue(it)
		}.launchIn(viewModelScope)
	}

}