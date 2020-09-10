package com.coders.chatapplication.domain.usecase.chat

import com.coders.chatapplication.commons.domain.exception.Failure
import com.coders.chatapplication.commons.domain.response.Either
import com.coders.chatapplication.commons.domain.usecase.UseCase
import com.coders.chatapplication.domain.di.DefaultDispatcher
import com.coders.chatapplication.domain.repository.ChatManager
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class UnSubscribeFromChannel @Inject constructor(
    @DefaultDispatcher
    defaultDispatcher: CoroutineDispatcher,
    private val chatManager: ChatManager
) : UseCase<Long, Unit>(defaultDispatcher) {
    override suspend fun execute(params: Long): Either<Failure, Unit> {
        chatManager.unSubscribeFromEvents()
        return Either.Right(Unit)
    }
}