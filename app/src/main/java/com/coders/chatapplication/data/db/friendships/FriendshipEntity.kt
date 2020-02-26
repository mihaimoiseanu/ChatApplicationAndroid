package com.coders.chatapplication.data.db.friendships

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import androidx.room.TypeConverters
import com.coders.chatapplication.data.db.Converters
import com.coders.chatapplication.data.db.user.UserEntity
import com.coders.chatapplication.domain.model.FriendshipStatus

@Entity(tableName = "friendship")
@TypeConverters(Converters::class)
data class FriendshipEntity(
	@PrimaryKey(autoGenerate = true)
	val id: Long? = null,
	@ColumnInfo
	val lastUserModifyId: Long,
	val status: FriendshipStatus? = null,
	val userId: Long
)

data class FriendshipUserEntity(
	@Embedded val user: UserEntity,

	@Relation(
		parentColumn = "userId",
		entityColumn = "userId"
	)
	val friendshipEntity: FriendshipEntity
)