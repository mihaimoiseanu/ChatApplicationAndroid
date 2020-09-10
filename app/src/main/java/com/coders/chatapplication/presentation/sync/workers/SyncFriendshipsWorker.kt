package com.coders.chatapplication.presentation.sync.workers

import android.content.Context
import androidx.hilt.Assisted
import androidx.hilt.work.WorkerInject
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.coders.chatapplication.domain.repository.FriendshipRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SyncFriendshipsWorker
@WorkerInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val friendshipRepository: FriendshipRepository
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            friendshipRepository.update()
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}