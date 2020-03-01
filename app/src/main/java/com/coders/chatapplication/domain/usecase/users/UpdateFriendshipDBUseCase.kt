package com.coders.chatapplication.domain.usecase.users

import com.coders.chatapplication.commons.domain.exception.Failure
import com.coders.chatapplication.commons.domain.response.Either
import com.coders.chatapplication.commons.domain.usecase.NoParams
import com.coders.chatapplication.commons.domain.usecase.UseCase
import com.coders.chatapplication.domain.repository.FriendshipRepository
import java.io.IOException

class UpdateFriendshipDBUseCase(private val friendshipRepository: FriendshipRepository) :
	UseCase<NoParams, Unit>() {

	override suspend fun execute(params: NoParams): Either<Failure, Unit> {
		return try {
			friendshipRepository.update()
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