package com.coders.chatapplication.domain.usecase.rooms

import com.coders.chatapplication.commons.domain.exception.Failure
import com.coders.chatapplication.commons.domain.response.Either
import com.coders.chatapplication.commons.domain.usecase.UseCase
import com.coders.chatapplication.domain.model.RoomModel
import com.coders.chatapplication.domain.repository.RoomRepository
import java.io.IOException

class CreateRoomUseCase(
	private val roomRepository: RoomRepository
) : UseCase<CreateRoomUseCase.Params, RoomModel>() {

	override suspend fun execute(params: Params): Either<Failure, RoomModel> {
		return try {
			val room = roomRepository.createRoom(params.name)
			Either.Right(room)
		} catch (e: Exception) {
			if (e is IOException) {
				Either.Left(Failure.NetworkConnection)
			} else {
				Either.Left(Failure.FeatureFailure(e))
			}
		}
	}

	data class Params(val name: String)
}