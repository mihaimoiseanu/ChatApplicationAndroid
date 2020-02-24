package com.coders.chatapplication.domain.usecase.users

import com.coders.chatapplication.commons.domain.exception.Failure
import com.coders.chatapplication.commons.domain.response.Either
import com.coders.chatapplication.commons.domain.usecase.UseCase
import com.coders.chatapplication.domain.repository.UserRepository
import java.io.IOException

class DeleteFriendshipUseCase(
	private val userRepository: UserRepository
) : UseCase<Long, Unit>() {
	override suspend fun execute(params: Long): Either<Failure, Unit> {
		return try {
			userRepository.deleteFriendship(params)
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