package com.coders.chatapplication.data.db.friendships

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface FriendshipDao {

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertFriendships(vararg friendshipEntity: FriendshipEntity)

	@Delete
	fun deleteFriendship(friendshipId: Long)

	@Transaction
	@Query("select * from friendship")
	fun getAllFriendships(): List<FriendshipUserEntity>
}