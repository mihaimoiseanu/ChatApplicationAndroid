package com.coders.chatapplication

import android.app.Application
import androidx.work.*
import com.coders.chatapplication.presentation.sync.workers.SyncFriendshipsWorker
import com.coders.chatapplication.presentation.sync.workers.SyncMessagesWorker
import com.coders.chatapplication.presentation.sync.workers.SyncRoomsWorker
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import java.util.concurrent.TimeUnit

@HiltAndroidApp
class MainApplication : Application() {
	override fun onCreate() {
		super.onCreate()
		if (BuildConfig.DEBUG) {
			Timber.plant(Timber.DebugTree())
		}
	}


	fun enqueueWork() {
		val constraint = Constraints.Builder()
			.setRequiredNetworkType(NetworkType.CONNECTED)
			.build()
		val periodicMessagesWork =
			PeriodicWorkRequest.Builder(SyncMessagesWorker::class.java, 15L, TimeUnit.MINUTES)
				.addTag("sync-message")
				.setConstraints(constraint)
				.build()
		WorkManager.getInstance(this).enqueueUniquePeriodicWork(
			"sync-messages",
			ExistingPeriodicWorkPolicy.KEEP,
			periodicMessagesWork
		)

		val periodicRoomsWorker =
			PeriodicWorkRequest.Builder(SyncRoomsWorker::class.java, 15L, TimeUnit.MINUTES)
				.addTag("sync-rooms")
				.setConstraints(constraint)
				.build()
		WorkManager.getInstance(this).enqueueUniquePeriodicWork(
			"sync-rooms",
			ExistingPeriodicWorkPolicy.KEEP,
			periodicRoomsWorker
		)

		val periodicFriendshipWorker =
			PeriodicWorkRequest.Builder(SyncFriendshipsWorker::class.java, 15L, TimeUnit.MINUTES)
				.addTag("sync-friendship")
				.setConstraints(constraint)
				.build()
		WorkManager.getInstance(this).enqueueUniquePeriodicWork(
			"sync-friendship",
			ExistingPeriodicWorkPolicy.KEEP,
			periodicFriendshipWorker
		)
	}
}