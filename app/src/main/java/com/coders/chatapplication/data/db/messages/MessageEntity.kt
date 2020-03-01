package com.coders.chatapplication.data.db.messages

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class MessageEntity(
	@PrimaryKey
	@ColumnInfo(index = true)
	val messageId: Long,
	var message: String,
	var sentAt: Long,
	val roomId: Long,
	var senderId: Long
)
