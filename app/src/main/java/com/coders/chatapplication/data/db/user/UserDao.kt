package com.coders.chatapplication.data.db.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insert(vararg userEntity: UserEntity)

	@Query("select * from users")
	fun getAllUsers(): List<UserEntity>

	@Query("select * from users where userId = :userId")
	suspend fun getUserById(userId: Long): UserEntity
}