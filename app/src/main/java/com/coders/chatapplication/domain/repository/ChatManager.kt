package com.coders.chatapplication.domain.repository

import com.coders.chatapplication.data.net.models.EventDTO
import com.coders.chatapplication.data.net.models.EventType

interface ChatManager {
	fun connect()
	fun subscribeForEvents()
	fun unSubscribeFromEvents()
	fun disconnect()
	fun sendEvent(type: EventType, eventDTO: EventDTO)
}