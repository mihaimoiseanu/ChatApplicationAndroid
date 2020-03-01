package com.coders.chatapplication.data.db.friendships

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation
import androidx.room.TypeConverters
import com.coders.chatapplication.data.db.Converters
import com.coders.chatapplication.data.db.user.UserEntity
import com.coders.chatapplication.domain.model.FriendshipStatus

@Entity(tableName = "friendship", indices = [Index("userId", unique = true)])
@TypeConverters(Converters::class)
data class FriendshipEntity(
	@ColumnInfo
	val lastUserModifyId: Long?,
	val status: FriendshipStatus? = null,
	@PrimaryKey
	val userId: Long
)

data class FriendshipUserEntity(
	@Embedded val friendshipEntity: FriendshipEntity,
	@Relation(
		parentColumn = "userId",
		entityColumn = "userId"
	)
	val user: UserEntity
)