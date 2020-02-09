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

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insert(vararg rooms: RoomEntity)

	@Transaction
	@Query("select * from rooms where roomId = :roomId")
	fun getRoomWithUsers(roomId: Long): Flow<RoomWithUsers>
}