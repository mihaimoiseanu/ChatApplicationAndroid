package com.coders.chatapplication.data.net

import com.coders.chatapplication.data.db.friendships.FriendshipEntity
import com.coders.chatapplication.data.db.friendships.FriendshipUserEntity
import com.coders.chatapplication.data.db.messages.MessageEntity
import com.coders.chatapplication.data.db.room.RoomEntity
import com.coders.chatapplication.data.db.room.RoomWithMessages
import com.coders.chatapplication.data.db.room.RoomWithUsers
import com.coders.chatapplication.data.db.user.UserEntity
import com.coders.chatapplication.data.net.models.FriendshipResponse
import com.coders.chatapplication.data.net.models.MessageResponse
import com.coders.chatapplication.data.net.models.RoomResponse
import com.coders.chatapplication.data.net.models.UserResponse
import com.coders.chatapplication.domain.model.FriendshipModel
import com.coders.chatapplication.domain.model.MessageModel
import com.coders.chatapplication.domain.model.RoomModel
import com.coders.chatapplication.domain.model.UserModel

fun UserResponse.asDomain(): UserModel {
	return UserModel(id, email, firstName, lastName)
}

fun UserResponse.asEntity(): UserEntity {
	return UserEntity(id!!, email!!, firstName, lastName)
}

fun UserModel.asEntity() = UserEntity(id!!, email!!, firstName, lastName)

fun UserEntity.asDomain(): UserModel {
	return UserModel(userId, email, firstName, lastName)
}

fun UserModel.asResponse(): UserResponse {
	return UserResponse(id, email, firstName, lastName, pass)
}

fun FriendshipResponse.asDomain(): FriendshipModel {
	return FriendshipModel(status, user.asDomain(), lastUserActioned)
}

fun FriendshipResponse.asEntity() = FriendshipEntity(
	lastUserModifyId = lastUserActioned,
	status = status,
	userId = user.id!!
)

fun FriendshipUserEntity.asDomain(): FriendshipModel {
	return FriendshipModel(
		friendshipEntity.status!!,
		user.asDomain(),
		friendshipEntity.lastUserModifyId
	)
}

fun FriendshipModel.asResponse() =
	FriendshipResponse(userModel.asResponse(), friendshipStatus)

fun RoomWithUsers.asDomain(): RoomModel {
	return RoomModel(
		room.roomId,
		room.name,
		users = users.map { it.asDomain() }
	)
}

fun RoomWithMessages.asDomain(): RoomModel {
	return RoomModel(
		room.roomId,
		room.name,
		users = users?.map { it.asDomain() },
		lastMessage = message?.asDomain()
	)
}

fun RoomResponse.asDomain() = RoomModel(
	id, name, null, users?.map { it.asDomain() }
)

fun RoomModel.asEntity() = RoomEntity(
	id, name, lastMessage?.id
)

fun MessageModel.asResponse() =
	MessageResponse(
		id, message, sender, roomId, sentAt
	)

fun MessageEntity.asDomain() =
	MessageModel(messageId, message, senderId, sentAt, roomId)

fun MessageResponse.asDomain() =
	MessageModel(
		id, message!!, senderId!!, sentAt, roomId!!
	)

fun MessageModel.asEntity() =
	MessageEntity(
		id!!, message, sentAt!!, roomId, sender
	)