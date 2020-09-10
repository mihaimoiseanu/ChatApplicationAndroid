package com.coders.chatapplication.domain.usecase.chat

import com.coders.chatapplication.commons.domain.exception.Failure
import com.coders.chatapplication.commons.domain.response.Either
import com.coders.chatapplication.commons.domain.usecase.UseCase
import com.coders.chatapplication.domain.di.DefaultDispatcher
import com.coders.chatapplication.domain.repository.MessageRepository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class UpdateMessagesUseCase
@Inject constructor(
    @DefaultDispatcher
    defaultDispatcher: CoroutineDispatcher, private val messageRepository: MessageRepository
) : UseCase<Long, Unit>(defaultDispatcher) {

    override suspend fun execute(params: Long): Either<Failure, Unit> {
        return try {
            messageRepository.updateMessages(params)
            Either.Right(Unit)
        } catch (e: Exception) {
            Either.Left(Failure.FeatureFailure(e))
        }
    }
}