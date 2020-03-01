package com.coders.chatapplication.data.db.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface RoomDao {

	@Transaction
	@Query("select * from rooms")
	fun getRoomsWithUsers(): Flow<List<RoomWithUsers>>

	@Transaction
	@Query("select * from rooms")
	fun getRoomWithMessage(): Flow<List<RoomWithMessages>>

	@Query("select * from rooms")
	fun getRooms(): List<RoomEntity>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insert(vararg rooms: RoomEntity)

	@Query("delete from rooms where roomId not in (:roomIds)")
	suspend fun deleteRooms(roomIds: List<Long>)

	@Query("delete from rooms where roomId = :roomId")
	suspend fun deleteRoom(roomId: Long)

	@Transaction
	@Query("select * from rooms where roomId = :roomId")
	fun getRoomWithUsers(roomId: Long): Flow<RoomWithUsers>

	@Query("update rooms set lastMessageId = :messageId where roomId = :roomId")
	suspend fun updateRoomLastMessage(roomId: Long, messageId: Long)
}