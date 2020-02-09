package com.coders.chatapplication.data.db.room

import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.Junction
import androidx.room.OnConflictStrategy
import androidx.room.Relation
import com.coders.chatapplication.data.db.user.UserEntity

@Entity(primaryKeys = ["roomId", "userId"])
data class UserRoomCrossRef(
	val roomId: Long,
	val userId: Long
)

data class RoomWithUsers(
	@Embedded val room: RoomEntity,
	@Relation(
		parentColumn = "roomId",
		entityColumn = "userId",
		associateBy = Junction(UserRoomCrossRef::class)
	)
	val users: List<UserEntity>
)

@Dao
interface UserRoomDao {

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insert(vararg userRooms: UserRoomCrossRef)

}