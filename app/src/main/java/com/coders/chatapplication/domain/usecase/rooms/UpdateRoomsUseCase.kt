package com.coders.chatapplication.domain.usecase.rooms

import com.coders.chatapplication.commons.domain.exception.Failure
import com.coders.chatapplication.commons.domain.response.Either
import com.coders.chatapplication.commons.domain.usecase.NoParams
import com.coders.chatapplication.commons.domain.usecase.UseCase
import com.coders.chatapplication.domain.di.DefaultDispatcher
import com.coders.chatapplication.domain.repository.RoomRepository
import kotlinx.coroutines.CoroutineDispatcher
import java.io.IOException
import javax.inject.Inject

class UpdateRoomsUseCase @Inject constructor(
    @DefaultDispatcher
    defaultDispatcher: CoroutineDispatcher,
    private val roomRepository: RoomRepository
) : UseCase<NoParams, Unit>(defaultDispatcher) {
    override suspend fun execute(params: NoParams): Either<Failure, Unit> {
        return try {
            roomRepository.updateRooms()
            Either.Right(Unit)
        } catch (e: Exception) {
            if (e is IOException) {
                Either.Left(Failure.NetworkConnection)
            } else {
                Either.Left(Failure.FeatureFailure(e))
            }
        }
    }
}