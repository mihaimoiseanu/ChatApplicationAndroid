package com.coders.chatapplication.data.repository.chat

import com.coders.chatapplication.domain.model.MessageModel
import com.coders.chatapplication.domain.repository.ChatRepository
import com.coders.stompclient.StompClient
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ChatRepositoryImpl(
	private val stompClient: StompClient,
	private val gson: Gson) : ChatRepository {

	override fun subscribeToRoom(scope: CoroutineScope, roomId: Long): Channel<MessageModel> {
		val channel = Channel<MessageModel>()
		stompClient
			.subscribePath("/chat-replay/${roomId}/message")
			.consumeAsFlow().map {
				gson.fromJson<MessageModel>(it.payload, MessageModel::class.java)
			}.onEach {
				scope.launch { channel.send(it) }
			}.launchIn(scope)
		return channel
	}

	override fun unsubscribeFromRoom(roomId: Long) {
		stompClient.unsubscribePath("/chat-replay/$roomId/message")
	}

	override fun sendMessage(roomId: Long, messageModel: MessageModel) {
		stompClient.send(
			"/chat/$roomId/message",
			gson.toJson(messageModel)
		)
	}
}