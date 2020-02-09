package com.coders.chatapplication.data.db.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
	@PrimaryKey var userId: Long,
	@ColumnInfo(name = "email") var email: String,
	@ColumnInfo(name = "first_name") var firstName: String?,
	@ColumnInfo(name = "last_name") var lastName: String?
)