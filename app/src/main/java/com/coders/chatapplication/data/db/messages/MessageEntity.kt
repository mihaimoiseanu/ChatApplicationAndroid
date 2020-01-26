package com.coders.chatapplication.data.db.messages

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class MessageEntity(
	@PrimaryKey val id: Long,
	var message: String,
	@ColumnInfo(name = "sent_at")
	var sentAt: Long,
	@ColumnInfo(name = "room_id")
	val roomId: Long,
	@ColumnInfo(name = "sender_id")
	var senderId: Long
)
