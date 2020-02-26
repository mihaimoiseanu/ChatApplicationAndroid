package com.coders.chatapplication.data.net.socket

import com.coders.chatapplication.data.di.BASE_URL
import com.coders.chatapplication.data.net.asDomain
import com.coders.chatapplication.data.net.asResponse
import com.coders.chatapplication.data.net.models.Event
import com.coders.chatapplication.data.net.models.EventDTO
import com.coders.chatapplication.data.net.models.EventType
import com.coders.chatapplication.data.net.models.FriendshipResponse
import com.coders.chatapplication.data.net.models.MessageResponse
import com.coders.chatapplication.data.net.models.RoomResponse
import com.coders.chatapplication.data.sharedprefs.SharedPrefs
import com.coders.chatapplication.domain.model.MessageModel
import com.coders.chatapplication.domain.repository.ChatManager
import com.coders.chatapplication.domain.repository.MessageRepository
import com.coders.stompclient.Stomp
import com.coders.stompclient.provider.ConnectionProvider
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import kotlin.coroutines.CoroutineContext

@Suppress("EXPERIMENTAL_API_USAGE")
class ChatManagerImpl(
	okHttpClient: OkHttpClient,
	private val gson: Gson,
	private val sharedPrefs: SharedPrefs,
	private val messageRepository: MessageRepository
) : CoroutineScope, ChatManager {

	override val coroutineContext: CoroutineContext
		get() = Dispatchers.IO + SupervisorJob()

	private val stompClient = Stomp.over(
		type = ConnectionProvider.Type.OKHTTP,
		uri = "ws://$BASE_URL/websocket",
		okHttpClient = okHttpClient
	)
	private val userId: Long
		get() = sharedPrefs.userId

	init {
		stompClient.connectionChannel.consumeAsFlow().onEach {
			if (it) {
				subscribeForEvents()
			} else {
				launch {
					delay(3_000)
					connect()
				}
			}
		}.launchIn(this)
	}

	override fun connect() {
		stompClient.connect()
	}

	override fun subscribeForEvents() {
		stompClient.subscribePath("/events-replay/$userId")
			.consumeAsFlow()
			.map {
				val type = EventType.valueOf(
					gson.fromJson<JsonObject>(
						it.payload,
						JsonObject::class.java
					)["type"].asString
				)
				when (type) {
					EventType.FRIENDSHIP_CREATED,
					EventType.FRIENDSHIP_UPDATED,
					EventType.FRIENDSHIP_DELETED -> gson.fromJson<Event<EventDTO>>(
						it.payload,
						object : TypeToken<Event<FriendshipResponse>>() {}.type
					)
					EventType.ROOM_CREATED,
					EventType.ROOM_UPDATED,
					EventType.ROOM_DELETED -> gson.fromJson(
						it.payload,
						object : TypeToken<Event<RoomResponse>>() {}.type
					)
					EventType.MESSAGE_CREATED,
					EventType.MESSAGE_UPDATED,
					EventType.MESSAGE_DELETED -> gson.fromJson(
						it.payload,
						object : TypeToken<Event<MessageResponse>>() {}.type
					)
				}
			}
			.onEach {
				launch {
					handleEventReceived(it)
				}
			}
			.launchIn(this)
	}

	override fun unSubscribeFromEvents() {
		stompClient.unsubscribePath("/events-replay/$userId")
	}

	override fun sendMessage(roomId: Long, messageModel: MessageModel) {
		sendEvent(EventType.MESSAGE_CREATED, messageModel.asResponse())
	}

	private fun sendEvent(type: EventType, eventDTO: EventDTO) {
		stompClient.send(
			"/events/$userId",
			gson.toJson(Event(type, eventDTO))
		)
	}

	override fun disconnect() {
		stompClient.disconnect()
	}

	private suspend fun handleEventReceived(event: Event<EventDTO>) {
		when (event.type) {
			EventType.FRIENDSHIP_CREATED -> TODO()
			EventType.FRIENDSHIP_UPDATED -> TODO()
			EventType.FRIENDSHIP_DELETED -> TODO()
			EventType.ROOM_CREATED -> TODO()
			EventType.ROOM_UPDATED -> TODO()
			EventType.ROOM_DELETED -> TODO()
			EventType.MESSAGE_CREATED -> messageRepository.insertMessage((event.eventDTO as MessageResponse).asDomain())
			EventType.MESSAGE_UPDATED -> messageRepository.insertMessage((event.eventDTO as MessageResponse).asDomain())
			EventType.MESSAGE_DELETED -> messageRepository.deleteMessage((event.eventDTO as MessageResponse).asDomain())
		}
	}
}