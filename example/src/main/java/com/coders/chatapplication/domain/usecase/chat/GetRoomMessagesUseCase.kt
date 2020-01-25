package com.coders.chatapplication.domain.usecase.chat

import com.coders.chatapplication.commons.domain.exception.Failure
import com.coders.chatapplication.commons.domain.response.Either
import com.coders.chatapplication.commons.domain.usecase.UseCase
import com.coders.chatapplication.domain.model.MessageModel
import com.coders.chatapplication.domain.repository.RoomRepository
import java.io.IOException

class GetRoomMessagesUseCase(private val roomRepository: RoomRepository) :
	UseCase<Long, List<MessageModel>>() {

	override suspend fun execute(params: Long): Either<Failure, List<MessageModel>> {
		return try {
			val messages = roomRepository.getRoomMessages(params)
			Either.Right(messages)
		} catch (e: Exception) {
			if (e is IOException) {
				Either.Left(Failure.NetworkConnection)
			} else {
				Either.Left(Failure.FeatureFailure(e))
			}
		}
	}
}