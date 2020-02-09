package com.coders.chatapplication.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.coders.chatapplication.data.db.messages.MessageDao
import com.coders.chatapplication.data.db.messages.MessageEntity
import com.coders.chatapplication.data.db.room.RoomDao
import com.coders.chatapplication.data.db.room.RoomEntity
import com.coders.chatapplication.data.db.room.UserRoomCrossRef
import com.coders.chatapplication.data.db.room.UserRoomDao
import com.coders.chatapplication.data.db.user.UserDao
import com.coders.chatapplication.data.db.user.UserEntity

@Database(
	entities = [UserEntity::class, RoomEntity::class, MessageEntity::class, UserRoomCrossRef::class],
	version = 1,
	exportSchema = false
)
abstract class ChatDatabase : RoomDatabase() {

	abstract fun userDao(): UserDao
	abstract fun roomDao(): RoomDao
	abstract fun messageDao(): MessageDao
	abstract fun userRoomDao(): UserRoomDao

}