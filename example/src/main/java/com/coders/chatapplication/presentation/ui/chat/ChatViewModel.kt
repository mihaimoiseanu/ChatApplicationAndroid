package com.coders.chatapplication.presentation.ui.chat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.coders.chatapplication.domain.model.MessageModel
import com.coders.chatapplication.domain.usecase.chat.GetRoomMessagesUseCase
import com.coders.chatapplication.domain.usecase.chat.SendMessageUseCase
import com.coders.chatapplication.domain.usecase.chat.SubscribeToRoomUseCase
import com.coders.chatapplication.domain.usecase.chat.UnSubscribeFromChannel
import com.coders.chatapplication.presentation.commons.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatViewModel(
	private val getRoomMessagesUseCase: GetRoomMessagesUseCase,
	private val subscribeToRoomUseCase: SubscribeToRoomUseCase,
	private val unSubscribeFromChannel: UnSubscribeFromChannel,
	private val sendMessageUseCase: SendMessageUseCase
) : BaseViewModel() {

	var roomId: Long = -1

	var roomMessages = MutableLiveData<List<MessageModel>>()
	var messageReceived = MutableLiveData<MessageModel>()

	fun getRoomMessages() {
		viewModelScope.launch {
			withContext(Dispatchers.IO) {
				getRoomMessagesUseCase(this, roomId) {
					it.either(::handleFailure, ::handleGetMessages)
				}
			}
		}
	}

	fun subscribeToRoom() {
		subscribeToRoomUseCase(
			viewModelScope,
			SubscribeToRoomUseCase.Params(viewModelScope, roomId)
		) {
			it.either(::handleFailure, ::handleSuccessSubscribe)
		}
	}

	fun sendMessage(message: String) {
		sendMessageUseCase(viewModelScope, SendMessageUseCase.Params(roomId, message))
	}

	fun unsubscribeFromRoom() {
		unSubscribeFromChannel(viewModelScope, roomId)
	}

	private fun handleGetMessages(messages: List<MessageModel>) {
		roomMessages.postValue(messages)
	}

	private fun handleSuccessSubscribe(channel: Channel<MessageModel>) {
		channel.consumeAsFlow().onEach {
			messageReceived.postValue(it)
		}.launchIn(viewModelScope)
	}
}