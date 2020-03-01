package com.coders.chatapplication.data.db.messages

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insert(vararg messages: MessageEntity)

	@Delete
	suspend fun delete(vararg messages: MessageEntity)

	@Query("delete from messages where roomId = :roomId")
	suspend fun delete(roomId: Long)

	@Query("select * from messages where roomId = :roomId order by sentAt desc")
	fun getMessagesForRoom(roomId: Long): Flow<List<MessageEntity>>
}