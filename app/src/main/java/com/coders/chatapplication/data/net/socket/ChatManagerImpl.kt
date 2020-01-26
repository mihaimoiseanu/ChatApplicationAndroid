package com.coders.chatapplication.data.net.socket

import com.coders.chatapplication.data.db.messages.MessageDao
import com.coders.chatapplication.data.db.messages.MessageEntity
import com.coders.chatapplication.data.net.models.MessageResponse
import com.coders.chatapplication.domain.model.MessageModel
import com.coders.chatapplication.domain.repository.ChatManager
import com.coders.stompclient.Stomp
import com.coders.stompclient.provider.ConnectionProvider
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import kotlin.coroutines.CoroutineContext

class ChatManagerImpl(
	okHttpClient: OkHttpClient,
	private val messageDao: MessageDao,
	private val gson: Gson
) : CoroutineScope, ChatManager {

	override val coroutineContext: CoroutineContext
		get() = Dispatchers.IO + SupervisorJob()

	private val stompClient = Stomp.over(
		type = ConnectionProvider.Type.OKHTTP,
		uri = "ws://192.168.0.118:8080/websocket",
		okHttpClient = okHttpClient
	)


	override fun connect() {
		stompClient.connect()
	}

	override fun subscribeToRoom(roomId: Long) {
		stompClient
			.subscribePath("/chat-replay/${roomId}/message")
			.consumeAsFlow().map {
				gson.fromJson<MessageResponse>(it.payload, MessageResponse::class.java)
			}.onEach {
				launch {
					val messageEntity =
						MessageEntity(
							it.id!!,
							it.message!!,
							it.sentAt!!,
							it.roomId!!,
							it.senderId!!
						)
					messageDao.insert(messageEntity)
				}
			}.launchIn(this)
	}

	override fun unSubscribeFromRoom(roomId: Long) {
		stompClient.unsubscribePath("/chat-replay/$roomId/message")
	}

	override fun sendMessage(roomId: Long, messageModel: MessageModel) {
		val messageResponse = MessageResponse(
			messageModel.id,
			messageModel.message,
			messageModel.sender,
			roomId,
			messageModel.sentAt
		)
		stompClient.send(
			"/chat/$roomId/message",
			gson.toJson(messageResponse)
		)
	}

	override fun disconnect() {
		stompClient.disconnect()
	}


}