package com.coders.chatapplication.domain.repository

import com.coders.chatapplication.domain.model.MessageModel
import kotlinx.coroutines.flow.Flow

interface ChatRepository {

	suspend fun getMessages(roomId: Long): Flow<List<MessageModel>>

	suspend fun updateMessages(roomId: Long)
}