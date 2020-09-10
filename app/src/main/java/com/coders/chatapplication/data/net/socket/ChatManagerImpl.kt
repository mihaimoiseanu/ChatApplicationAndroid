package com.coders.chatapplication.data.net.socket

import com.coders.chatapplication.data.di.NetworkModule.BASE_URL
import com.coders.chatapplication.data.net.asDomain
import com.coders.chatapplication.data.net.models.*
import com.coders.chatapplication.data.sharedprefs.SharedPrefs
import com.coders.chatapplication.domain.repository.ChatManager
import com.coders.chatapplication.domain.repository.FriendshipRepository
import com.coders.chatapplication.domain.repository.MessageRepository
import com.coders.chatapplication.domain.repository.RoomRepository
import com.coders.stompclient.Stomp
import com.coders.stompclient.provider.ConnectionProvider
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
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
	private val gson: Gson,
	private val sharedPrefs: SharedPrefs,
	private val messageRepository: MessageRepository,
	private val roomRepository: RoomRepository,
	private val friendshipRepository: FriendshipRepository
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

	override fun sendEvent(type: EventType, eventDTO: EventDTO) {
		stompClient.send(
			"/events/$userId",
			gson.toJson(Event(type, eventDTO))
		)
	}

	override fun disconnect() {
		stompClient.disconnect()
	}

	private fun handleEventReceived(event: Event<EventDTO>) {
		when (event.type) {
			EventType.FRIENDSHIP_CREATED -> launch {
				val friendship = event.eventDTO as FriendshipResponse
				friendshipRepository.insertFriendship(friendship.asDomain())
			}
			EventType.FRIENDSHIP_UPDATED -> launch {
				val friendship = event.eventDTO as FriendshipResponse
				friendshipRepository.updateFriendship(friendship.asDomain())
			}
			EventType.FRIENDSHIP_DELETED -> launch {
				val friendship = event.eventDTO as FriendshipResponse
				friendshipRepository.deleteFriendship(friendship.asDomain())
			}
			EventType.ROOM_CREATED -> launch {
				val room = event.eventDTO as RoomResponse
				roomRepository.insertRoom(room.asDomain())
			}
			EventType.ROOM_UPDATED -> launch {
				val room = event.eventDTO as RoomResponse
				roomRepository.updateRoom(room.asDomain())
			}
			EventType.ROOM_DELETED -> launch {
				val room = event.eventDTO as RoomResponse
				roomRepository.deleteRoom(room.asDomain())
			}
			EventType.MESSAGE_CREATED -> {
				val message = event.eventDTO as MessageResponse
				launch { messageRepository.insertMessage(message.asDomain()) }
				launch {
					roomRepository.updateRoomLastMessage(
						message.roomId!!,
						message.id!!
					)
				}
			}
			EventType.MESSAGE_UPDATED -> launch { messageRepository.insertMessage((event.eventDTO as MessageResponse).asDomain()) }
			EventType.MESSAGE_DELETED -> launch { messageRepository.deleteMessage((event.eventDTO as MessageResponse).asDomain()) }
		}
	}
}