package com.coders.chatapplication.domain.usecase.chat

import com.coders.chatapplication.commons.domain.exception.Failure
import com.coders.chatapplication.commons.domain.response.Either
import com.coders.chatapplication.commons.domain.usecase.UseCase
import com.coders.chatapplication.domain.repository.ChatRepository

class UnSubscribeFromChannel(
	private val chatRepository: ChatRepository
) : UseCase<Long, Unit>() {
	override suspend fun execute(params: Long): Either<Failure, Unit> {
		chatRepository.unsubscribeFromRoom(params)
		return Either.Right(Unit)
	}
}