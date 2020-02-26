package com.coders.chatapplication.domain.repository

import com.coders.chatapplication.domain.model.MessageModel

interface ChatManager {
	fun connect()
	fun subscribeForEvents()
	fun unSubscribeFromEvents()
	fun disconnect()
	fun sendMessage(roomId: Long, messageModel: MessageModel)
}