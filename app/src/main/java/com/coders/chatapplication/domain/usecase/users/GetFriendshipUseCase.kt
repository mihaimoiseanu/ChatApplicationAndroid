package com.coders.chatapplication.domain.usecase.users

import com.coders.chatapplication.commons.domain.exception.Failure
import com.coders.chatapplication.commons.domain.response.Either
import com.coders.chatapplication.commons.domain.usecase.UseCase
import com.coders.chatapplication.domain.di.DefaultDispatcher
import com.coders.chatapplication.domain.model.FriendshipModel
import com.coders.chatapplication.domain.repository.FriendshipRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import java.io.IOException
import javax.inject.Inject

class GetFriendshipUseCase @Inject constructor(
    @DefaultDispatcher
    defaultDispatcher: CoroutineDispatcher,
    private val friendshipRepository: FriendshipRepository
) : UseCase<Long, Flow<FriendshipModel>>(defaultDispatcher) {

    override suspend fun execute(params: Long): Either<Failure, Flow<FriendshipModel>> {
        return try {
            val result = friendshipRepository.getFriendship(params)
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