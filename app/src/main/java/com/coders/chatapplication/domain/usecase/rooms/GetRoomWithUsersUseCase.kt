package com.coders.chatapplication.domain.usecase.rooms

import com.coders.chatapplication.commons.domain.exception.Failure
import com.coders.chatapplication.commons.domain.response.Either
import com.coders.chatapplication.commons.domain.usecase.UseCase
import com.coders.chatapplication.domain.model.RoomModel
import com.coders.chatapplication.domain.repository.RoomRepository
import kotlinx.coroutines.flow.Flow
import java.io.IOException

class GetRoomWithUsersUseCase(
	private val roomRepository: RoomRepository
) : UseCase<Long, Flow<RoomModel>>() {

	override suspend fun execute(params: Long): Either<Failure, Flow<RoomModel>> {
		return try {
			val room = roomRepository.getRoomWithUsers(params)
			Either.Right(room)
		} catch (e: Exception) {
			if (e is IOException) {
				Either.Left(Failure.NetworkConnection)
			} else {
				Either.Left(Failure.FeatureFailure(e))
			}
		}
	}
}