package com.coders.chatapplication.domain.usecase.chat

import com.coders.chatapplication.commons.domain.exception.Failure
import com.coders.chatapplication.commons.domain.response.Either
import com.coders.chatapplication.commons.domain.usecase.NoResult
import com.coders.chatapplication.commons.domain.usecase.UseCase
import com.coders.chatapplication.data.net.asResponse
import com.coders.chatapplication.data.net.models.EventType
import com.coders.chatapplication.data.sharedprefs.SharedPrefs
import com.coders.chatapplication.domain.di.DefaultDispatcher
import com.coders.chatapplication.domain.model.MessageModel
import com.coders.chatapplication.domain.repository.ChatManager
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class SendMessageUseCase
@Inject constructor(
    @DefaultDispatcher
    defaultDispatcher: CoroutineDispatcher,
    private val chatManager: ChatManager,
    private val sharedPrefs: SharedPrefs
) : UseCase<SendMessageUseCase.Params, NoResult>(defaultDispatcher) {
    override suspend fun execute(params: Params): Either<Failure, NoResult> {
        val messageModel = MessageModel(
            message = params.message,
            sender = sharedPrefs.userId,
            sentAt = System.currentTimeMillis(),
            roomId = params.roomId
        )
        chatManager.sendEvent(
            EventType.MESSAGE_CREATED,
            messageModel.asResponse()
        )
        return Either.Right(NoResult)
    }

    data class Params(val roomId: Long, val message: String)
}