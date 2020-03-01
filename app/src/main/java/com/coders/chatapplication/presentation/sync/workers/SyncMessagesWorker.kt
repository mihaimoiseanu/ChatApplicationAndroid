package com.coders.chatapplication.presentation.sync.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.coders.chatapplication.MainApplication
import com.coders.chatapplication.data.db.room.RoomDao
import com.coders.chatapplication.domain.repository.MessageRepository
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.getKoin

//Maybe change this implementation
class SyncMessagesWorker(appContext: Context, params: WorkerParameters) :
	CoroutineWorker(appContext, params) {

	override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
		val roomDao = (applicationContext as MainApplication).getKoin().get<RoomDao>()
		val works = mutableListOf<Deferred<Unit>>()
		val messageRepository =
			(applicationContext as MainApplication).getKoin().get<MessageRepository>()
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