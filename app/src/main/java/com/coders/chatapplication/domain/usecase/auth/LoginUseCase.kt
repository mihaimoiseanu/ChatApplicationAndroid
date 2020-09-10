package com.coders.chatapplication.domain.usecase.auth

import com.coders.chatapplication.commons.domain.exception.Failure
import com.coders.chatapplication.commons.domain.response.Either
import com.coders.chatapplication.commons.domain.usecase.UseCase
import com.coders.chatapplication.domain.di.DefaultDispatcher
import com.coders.chatapplication.domain.model.UserModel
import com.coders.chatapplication.domain.repository.AuthRepository
import kotlinx.coroutines.CoroutineDispatcher
import java.io.IOException
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    @DefaultDispatcher
    defaultDispatcher: CoroutineDispatcher,
    private val repository: AuthRepository
) : UseCase<LoginUseCase.Params, UserModel>(defaultDispatcher) {

    override suspend fun execute(params: Params): Either<Failure, UserModel> {
        return try {
            val headlines = repository.login(params.email, params.password)
            Either.Right(headlines)
        } catch (e: Exception) {
            if (e is IOException) {
                Either.Left(Failure.NetworkConnection)
            } else {
                Either.Left(Failure.FeatureFailure(e))
            }
        }
    }

    data class Params(val email: String, val password: String)
}