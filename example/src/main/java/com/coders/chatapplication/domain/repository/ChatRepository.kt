package com.coders.chatapplication.domain.repository

import com.coders.chatapplication.domain.model.MessageModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel

interface ChatRepository {

	fun subscribeToRoom(scope: CoroutineScope, roomId:Long): Channel<MessageModel>

	fun unsubscribeFromRoom(roomId: Long)

	fun sendMessage(roomId: Long, messageModel: MessageModel)
}