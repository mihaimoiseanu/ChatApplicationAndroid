package com.coders.chatapplication.domain.usecase.users

import com.coders.chatapplication.commons.domain.exception.Failure
import com.coders.chatapplication.commons.domain.response.Either
import com.coders.chatapplication.commons.domain.usecase.UseCase
import com.coders.chatapplication.domain.di.DefaultDispatcher
import com.coders.chatapplication.domain.model.UserModel
import com.coders.chatapplication.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import java.io.IOException
import javax.inject.Inject

class SearchUsersUseCase @Inject constructor(
    @DefaultDispatcher
    defaultDispatcher: CoroutineDispatcher, private val userRepository: UserRepository
) : UseCase<String, List<UserModel>>(defaultDispatcher) {

    override suspend fun execute(params: String): Either<Failure, List<UserModel>> {
        return try {
            val result = userRepository.searchUsers(params)
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