package com.coders.chatapplication.domain.usecase.auth

import com.coders.chatapplication.commons.domain.exception.Failure
import com.coders.chatapplication.commons.domain.response.Either
import com.coders.chatapplication.commons.domain.usecase.UseCase
import com.coders.chatapplication.domain.model.UserModel
import com.coders.chatapplication.domain.repository.AuthRepository
import java.io.IOException

class RegisterUseCase(
	private val authRepository: AuthRepository
) : UseCase<UserModel, UserModel>() {

	override suspend fun execute(params: UserModel): Either<Failure, UserModel> {
		return try {
			val userCreated = authRepository.register(params)
			Either.Right(userCreated)
		} catch (e: Exception) {
			if (e is IOException) {
				Either.Left(Failure.NetworkConnection)
			} else {
				Either.Left(Failure.FeatureFailure(e))
			}
		}
	}
}