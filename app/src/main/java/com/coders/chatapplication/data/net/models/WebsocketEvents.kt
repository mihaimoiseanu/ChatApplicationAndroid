package com.coders.chatapplication.data.net.models

data class Event<T : EventDTO>(
	val type: EventType,
	val eventDTO: T
)

interface EventDTO

enum class EventType {

	FRIENDSHIP_CREATED,
	FRIENDSHIP_UPDATED,
	FRIENDSHIP_DELETED,

	ROOM_CREATED,
	ROOM_UPDATED,
	ROOM_DELETED,

	MESSAGE_CREATED,
	MESSAGE_UPDATED,
	MESSAGE_DELETED
}