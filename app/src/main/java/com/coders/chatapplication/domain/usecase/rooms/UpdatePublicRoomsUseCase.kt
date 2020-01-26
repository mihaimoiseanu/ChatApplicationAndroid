package com.coders.chatapplication.domain.usecase.rooms

import com.coders.chatapplication.commons.domain.exception.Failure
import com.coders.chatapplication.commons.domain.response.Either
import com.coders.chatapplication.commons.domain.usecase.NoParams
import com.coders.chatapplication.commons.domain.usecase.UseCase
import com.coders.chatapplication.domain.repository.RoomRepository
import java.io.IOException

class UpdatePublicRoomsUseCase(
	private val roomRepository: RoomRepository
) : UseCase<NoParams, Unit>() {
	override suspend fun execute(params: NoParams): Either<Failure, Unit> {
		return try {
			roomRepository.updatePublicRooms()
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