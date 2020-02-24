package com.coders.chatapplication.domain.usecase.users

import com.coders.chatapplication.commons.domain.exception.Failure
import com.coders.chatapplication.commons.domain.response.Either
import com.coders.chatapplication.commons.domain.usecase.UseCase
import com.coders.chatapplication.domain.model.FriendshipModel
import com.coders.chatapplication.domain.repository.UserRepository
import java.io.IOException

class GetFriendshipUseCase(
	private val userRepository: UserRepository
) : UseCase<Long, FriendshipModel>() {

	override suspend fun execute(params: Long): Either<Failure, FriendshipModel> {
		return try {
			val result = userRepository.getFriendship(params)
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