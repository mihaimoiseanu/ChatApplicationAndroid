package com.coders.chatapplication.presentation.sync.manager

import com.coders.chatapplication.commons.domain.exception.Failure
import com.coders.chatapplication.commons.domain.usecase.NoParams
import com.coders.chatapplication.domain.usecase.chat.UpdateMessagesUseCase
import com.coders.chatapplication.domain.usecase.rooms.UpdateRoomsUseCase
import com.coders.chatapplication.domain.usecase.users.UpdateFriendshipDBUseCase
import kotlinx.coroutines.CoroutineScope
import timber.log.Timber

//Do we really need this?
class SyncManager(
	private val updateFriendshipDBUseCase: UpdateFriendshipDBUseCase,
	private val updateRoomsUseCase: UpdateRoomsUseCase,
	private val updateMessagesUseCase: UpdateMessagesUseCase
) {

	fun updateFriendships(scope: CoroutineScope) {
		updateFriendshipDBUseCase(scope, NoParams) {
			it.either(::handleFailure)
		}
	}

	fun updateRoomsUseCase(scope: CoroutineScope) {
		updateRoomsUseCase(scope, NoParams) {
			it.either(::handleFailure)
		}
	}

	fun updateMessages(roomId: Long, scope: CoroutineScope) {
		updateMessagesUseCase(scope, roomId) {
			it.either(::handleFailure)
		}
	}

	private fun handleFailure(failure: Failure) {
		Timber.e(failure.exception)
	}
}