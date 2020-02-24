package com.coders.chatapplication.data.net

import com.coders.chatapplication.data.db.room.RoomWithUsers
import com.coders.chatapplication.data.db.user.UserEntity
import com.coders.chatapplication.data.net.models.FriendshipResponse
import com.coders.chatapplication.data.net.models.UserResponse
import com.coders.chatapplication.domain.model.FriendshipModel
import com.coders.chatapplication.domain.model.RoomModel
import com.coders.chatapplication.domain.model.UserModel

fun UserResponse.asDomain(): UserModel {
	return UserModel(id, email, firstName, lastName)
}

fun UserResponse.asEntity(): UserEntity {
	return UserEntity(id!!, email!!, firstName, lastName)
}

fun UserEntity.asDomain(): UserModel {
	return UserModel(userId, email, firstName, lastName)
}

fun UserModel.asResponse(): UserResponse {
	return UserResponse(id, email, firstName, lastName, pass)
}

fun FriendshipResponse.asDomain(): FriendshipModel {
	return FriendshipModel(status, user.asDomain(), lastUserActioned)
}

fun FriendshipModel.asResponse() =
	FriendshipResponse(userModel.asResponse(), friendshipStatus)

fun RoomWithUsers.asDomain(): RoomModel {
	return RoomModel(
		room.roomId,
		room.name,
		users.map { it.asDomain() }
	)
}