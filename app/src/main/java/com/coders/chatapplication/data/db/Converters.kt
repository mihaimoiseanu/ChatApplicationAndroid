package com.coders.chatapplication.data.db

import androidx.room.TypeConverter
import com.coders.chatapplication.domain.model.FriendshipStatus

class Converters {
	@TypeConverter
	fun frienshipStatusToInt(status: FriendshipStatus?): Int? = status?.ordinal

	@TypeConverter
	fun intToFriendship(int: Int?): FriendshipStatus? = int?.let { FriendshipStatus.values()[it] }
}