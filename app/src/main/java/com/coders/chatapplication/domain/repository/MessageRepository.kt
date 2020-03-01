package com.coders.chatapplication.domain.repository

import com.coders.chatapplication.domain.model.MessageModel
import kotlinx.coroutines.flow.Flow

interface MessageRepository {

	suspend fun getMessages(roomId: Long): Flow<List<MessageModel>>

	suspend fun insertMessage(messageModel: MessageModel)

	suspend fun deleteMessage(messageModel: MessageModel)

	suspend fun deleteMessages(roomId: Long)

	suspend fun updateMessages(roomId: Long)
}