package com.coders.chatapplication.data.db.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rooms")
data class RoomEntity(
	@PrimaryKey
	val roomId: Long,
	var name: String? = null
)