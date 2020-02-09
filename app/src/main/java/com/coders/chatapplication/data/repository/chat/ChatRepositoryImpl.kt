package com.coders.chatapplication.data.repository.chat

import com.coders.chatapplication.data.db.messages.MessageDao
import com.coders.chatapplication.data.db.messages.MessageEntity
import com.coders.chatapplication.data.net.api.RoomsService
import com.coders.chatapplication.domain.model.MessageModel
import com.coders.chatapplication.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ChatRepositoryImpl(
	private val roomsService: RoomsService,
	private val messageDao: MessageDao
) : ChatRepository {

	override suspend fun getMessages(roomId: Long): Flow<List<MessageModel>> {
		return messageDao
			.getMessagesForRoom(roomId)
			.map { list ->
				list.map { message ->
					MessageModel(
						message.messageId,
						message.message,
						message.senderId,
						message.sentAt
					)
				}
			}
	}

	override suspend fun updateMessages(roomId: Long) {
		val messages = roomsService.getRoomMessages(roomId).map {
			MessageEntity(it.id!!, it.message!!, it.sentAt!!, roomId, it.senderId!!)
		}
		messageDao.insert(*(messages.toTypedArray()))
	}
}