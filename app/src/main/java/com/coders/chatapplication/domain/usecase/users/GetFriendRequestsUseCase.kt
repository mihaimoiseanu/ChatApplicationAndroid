package com.coders.chatapplication.domain.usecase.users

import com.coders.chatapplication.commons.domain.exception.Failure
import com.coders.chatapplication.commons.domain.response.Either
import com.coders.chatapplication.commons.domain.usecase.NoParams
import com.coders.chatapplication.commons.domain.usecase.UseCase
import com.coders.chatapplication.domain.model.FriendshipModel
import com.coders.chatapplication.domain.model.FriendshipStatus
import com.coders.chatapplication.domain.repository.UserRepository
import java.io.IOException

class GetFriendRequestsUseCase(
	private val userRepository: UserRepository
) : UseCase<NoParams, List<FriendshipModel>>() {

	override suspend fun execute(params: NoParams): Either<Failure, List<FriendshipModel>> {
		return try {
			val result = userRepository.getFriendships(FriendshipStatus.PENDING)
			Either.Right(result)
		} catch (e: Exception) {
			if (e is IOException) {
				Either.Left(Failure.NetworkConnection)
			} else {
				Either.Left(Failure.FeatureFailure(e))
			}
		}
	}
}