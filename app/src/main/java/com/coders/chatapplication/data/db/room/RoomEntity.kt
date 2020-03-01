package com.coders.chatapplication.data.db.room

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.coders.chatapplication.data.db.messages.MessageEntity
import com.coders.chatapplication.data.db.user.UserEntity

@Entity(tableName = "rooms")
data class RoomEntity(
	@PrimaryKey
	@ColumnInfo(index = true)
	val roomId: Long? = null,
	var name: String? = null,
	var lastMessageId: Long? = null
)

data class RoomWithMessages(
	@Embedded val room: RoomEntity,
	@Relation(
		parentColumn = "lastMessageId",
		entityColumn = "messageId",
		entity = MessageEntity::class
	)
	val message: MessageEntity? = null,
	@Relation(
		parentColumn = "roomId",
		entityColumn = "userId",
		entity = UserEntity::class,
		associateBy = Junction(UserRoomCrossRef::class)
	)
	val users: List<UserEntity>? = null
)
