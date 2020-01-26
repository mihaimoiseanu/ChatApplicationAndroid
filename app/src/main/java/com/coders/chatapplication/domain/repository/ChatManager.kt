package com.coders.chatapplication.domain.repository

import com.coders.chatapplication.domain.model.MessageModel

interface ChatManager {
	fun connect()
	fun subscribeToRoom(roomId: Long)
	fun unSubscribeFromRoom(roomId: Long)
	fun disconnect()
	fun sendMessage(roomId: Long, messageModel: MessageModel)
}