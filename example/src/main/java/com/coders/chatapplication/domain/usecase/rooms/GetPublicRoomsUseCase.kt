package com.coders.chatapplication.domain.usecase.rooms

import com.coders.chatapplication.commons.domain.exception.Failure
import com.coders.chatapplication.commons.domain.response.Either
import com.coders.chatapplication.commons.domain.usecase.NoParams
import com.coders.chatapplication.commons.domain.usecase.UseCase
import com.coders.chatapplication.domain.model.RoomModel
import com.coders.chatapplication.domain.repository.RoomRepository
import java.io.IOException

class GetPublicRoomsUseCase(
	private val roomRepository: RoomRepository
) : UseCase<NoParams, List<RoomModel>>() {
	override suspend fun execute(params: NoParams): Either<Failure, List<RoomModel>> {
		return try {
			val rooms = roomRepository.getPublicRooms()
			Either.Right(rooms)
		} catch (e: Exception) {
			if (e is IOException) {
				Either.Left(Failure.NetworkConnection)
			} else {
				Either.Left(Failure.FeatureFailure(e))
			}
		}
	}
}