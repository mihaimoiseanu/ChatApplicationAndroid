package com.coders.chatapplication.presentation.sync.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.coders.chatapplication.MainApplication
import com.coders.chatapplication.domain.repository.RoomRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.getKoin

//Maybe change this implementation
class SyncRoomsWorker(appContext: Context, params: WorkerParameters) :
	CoroutineWorker(appContext, params) {

	override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
		val roomsRepository =
			(applicationContext as MainApplication).getKoin().get<RoomRepository>()
		try {
			roomsRepository.updateRooms()
			Result.success()
		} catch (e: Exception) {
			Result.failure()
		}
	}
}