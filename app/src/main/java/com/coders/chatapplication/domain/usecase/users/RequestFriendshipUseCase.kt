package com.coders.chatapplication.domain.usecase.users

import com.coders.chatapplication.commons.domain.exception.Failure
import com.coders.chatapplication.commons.domain.response.Either
import com.coders.chatapplication.commons.domain.usecase.UseCase
import com.coders.chatapplication.data.net.asResponse
import com.coders.chatapplication.data.net.models.EventType
import com.coders.chatapplication.domain.model.FriendshipModel
import com.coders.chatapplication.domain.repository.ChatManager
import java.io.IOException

class RequestFriendshipUseCase(private val chatManager: ChatManager) :
	UseCase<FriendshipModel, Unit>() {
	override suspend fun execute(params: FriendshipModel): Either<Failure, Unit> {
		return try {
			chatManager.sendEvent(
				EventType.FRIENDSHIP_CREATED,
				params.asResponse()
			)
			Either.Right(Unit)
		} catch (e: Exception) {
			if (e is IOException) {
				Either.Left(Failure.NetworkConnection)
			} else {
				Either.Left(Failure.FeatureFailure(e))
			}
		}
	}
}