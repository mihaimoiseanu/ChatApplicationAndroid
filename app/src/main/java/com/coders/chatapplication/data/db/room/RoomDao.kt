package com.coders.chatapplication.data.db.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RoomDao {

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insert(vararg rooms: RoomEntity)

	@Query("select * from rooms where is_private == 0")
	fun getPublicRooms(): Flow<List<RoomEntity>>

}