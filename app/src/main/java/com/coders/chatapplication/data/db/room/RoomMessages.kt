package com.coders.chatapplication.data.db.room

import androidx.room.Embedded
import androidx.room.Relation
import com.coders.chatapplication.data.db.messages.MessageEntity


data class RoomMessages(
	@Embedded val room: RoomEntity,
	@Relation(
		parentColumn = "id",
		entityColumn = "room_id"
	)
	val messages: List<MessageEntity>
)