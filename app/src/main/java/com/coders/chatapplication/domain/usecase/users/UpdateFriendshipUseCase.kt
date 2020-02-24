package com.coders.chatapplication.domain.usecase.users

import com.coders.chatapplication.commons.domain.exception.Failure
import com.coders.chatapplication.commons.domain.response.Either
import com.coders.chatapplication.commons.domain.usecase.UseCase
import com.coders.chatapplication.domain.model.FriendshipModel
import com.coders.chatapplication.domain.repository.UserRepository
import java.io.IOException

class UpdateFriendshipUseCase(private val userRepository: UserRepository) :
	UseCase<FriendshipModel, FriendshipModel>() {
	override suspend fun execute(params: FriendshipModel): Either<Failure, FriendshipModel> {
		return try {
			val result = userRepository.updateFriendship(params)
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