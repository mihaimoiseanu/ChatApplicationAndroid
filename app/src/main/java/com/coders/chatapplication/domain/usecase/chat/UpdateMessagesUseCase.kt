package com.coders.chatapplication.domain.usecase.chat

import com.coders.chatapplication.commons.domain.exception.Failure
import com.coders.chatapplication.commons.domain.response.Either
import com.coders.chatapplication.commons.domain.usecase.UseCase
import com.coders.chatapplication.domain.repository.ChatRepository

class UpdateMessagesUseCase(private val chatRepository: ChatRepository) :
	UseCase<Long, Unit>() {

	override suspend fun execute(params: Long): Either<Failure, Unit> {
		return try {
			chatRepository.updateMessages(params)
			Either.Right(Unit)
		} catch (e: Exception) {
			Either.Left(Failure.FeatureFailure(e))
		}
	}
}