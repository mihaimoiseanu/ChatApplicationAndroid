package com.coders.chatapplication.presentation.sync.workers

import android.content.Context
import androidx.hilt.Assisted
import androidx.hilt.work.WorkerInject
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.coders.chatapplication.data.db.room.RoomDao
import com.coders.chatapplication.domain.repository.MessageRepository
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class SyncMessagesWorker
@WorkerInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val roomDao: RoomDao,
    private val messageRepository: MessageRepository
) :
    CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val works = mutableListOf<Deferred<Unit>>()
        roomDao.getRooms().forEach {
            works.add(async {
                messageRepository.updateMessages(it.roomId!!)
            })
        }
        try {
            works.forEach { it.await() }
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}