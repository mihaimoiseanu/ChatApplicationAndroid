package com.coders.chatapplication.data.db.room

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.Junction
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Relation
import com.coders.chatapplication.data.db.user.UserEntity

@Entity(primaryKeys = ["roomId", "userId"], tableName = "user_room_cross_ref")
data class UserRoomCrossRef(
	@ColumnInfo(index = true)
	val roomId: Long,
	@ColumnInfo(index = true)
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

	@Query("delete from user_room_cross_ref where roomId not in (:roomIds)")
	suspend fun delete(roomIds: List<Long>)

	@Query("delete from user_room_cross_ref where roomId = :roomId")
	suspend fun delete(roomId: Long)

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insert(vararg userRooms: UserRoomCrossRef)

}