package com.coders.chatapplication.data.db.messages

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insert(vararg messages: MessageEntity)

	@Query("select * from messages where room_id = :roomId")
	fun getMessagesForRoom(roomId: Long): Flow<List<MessageEntity>>
}