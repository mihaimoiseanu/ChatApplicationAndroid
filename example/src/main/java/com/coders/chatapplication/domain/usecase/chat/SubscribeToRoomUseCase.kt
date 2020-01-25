package com.coders.chatapplication.domain.usecase.chat

import com.coders.chatapplication.commons.domain.exception.Failure
import com.coders.chatapplication.commons.domain.response.Either
import com.coders.chatapplication.commons.domain.usecase.UseCase
import com.coders.chatapplication.domain.model.MessageModel
import com.coders.chatapplication.domain.repository.ChatRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel

class SubscribeToRoomUseCase(
	private val chatRepository: ChatRepository
) : UseCase<SubscribeToRoomUseCase.Params, Channel<MessageModel>>() {

	override suspend fun execute(params: Params): Either<Failure, Channel<MessageModel>> {

		return Either.Right(chatRepository.subscribeToRoom(params.scope, params.roomId))

	}

	data class Params(val scope: CoroutineScope, val roomId: Long)
}