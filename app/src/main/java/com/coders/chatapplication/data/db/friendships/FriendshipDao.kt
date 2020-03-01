package com.coders.chatapplication.data.db.friendships

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface FriendshipDao {

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertFriendships(vararg friendshipEntity: FriendshipEntity)

	@Delete
	suspend fun deleteFriendship(vararg friendshipEntity: FriendshipEntity)

	@Transaction
	@Query("select * from friendship where status = :status")
	fun getAllFriendships(status: Int): Flow<List<FriendshipUserEntity>>

	@Transaction
	@Query("select * from friendship where userId = :otherUserId")
	fun getFriendshipWithUser(otherUserId: Long): Flow<FriendshipUserEntity?>

	@Query("select * from friendship where userId = :otherUserId")
	suspend fun getFriendship(otherUserId: Long): FriendshipEntity?
}