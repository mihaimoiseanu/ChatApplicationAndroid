package com.coders.chatapplication.domain.usecase.chat

import com.coders.chatapplication.commons.domain.exception.Failure
import com.coders.chatapplication.commons.domain.response.Either
import com.coders.chatapplication.commons.domain.usecase.NoParams
import com.coders.chatapplication.commons.domain.usecase.UseCase
import com.coders.chatapplication.domain.repository.ChatManager

class SubscribeForEventsUseCase(
	private val chatManager: ChatManager
) : UseCase<NoParams, Unit>() {

	override suspend fun execute(params: NoParams): Either<Failure, Unit> {
		return try {
			chatManager.subscribeForEvents()
			Either.Right(Unit)
		} catch (e: Exception) {
			Either.Left(Failure.FeatureFailure(e))
		}
	}
}