package com.coders.chatapplication.domain.usecase.chat

import com.coders.chatapplication.commons.domain.exception.Failure
import com.coders.chatapplication.commons.domain.response.Either
import com.coders.chatapplication.commons.domain.usecase.UseCase
import com.coders.chatapplication.data.sharedprefs.SharedPrefs
import com.coders.chatapplication.domain.model.MessageModel
import com.coders.chatapplication.domain.repository.ChatManager

class SendMessageUseCase(
	private val chatManager: ChatManager,
	private val sharedPrefs: SharedPrefs
) : UseCase<SendMessageUseCase.Params, Unit>() {
	override suspend fun execute(params: Params): Either<Failure, Unit> {
		val messageModel = MessageModel(
			message = params.message,
			sender = sharedPrefs.userId,
			sentAt = System.currentTimeMillis()
		)
		chatManager.sendMessage(params.roomId, messageModel)
		return Either.Right(Unit)
	}

	data class Params(val roomId: Long, val message: String)
}