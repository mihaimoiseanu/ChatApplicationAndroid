package com.coders.chatapplication

import android.app.Application
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.coders.chatapplication.data.di.dataModule
import com.coders.chatapplication.data.di.dbModule
import com.coders.chatapplication.data.di.networkingModule
import com.coders.chatapplication.domain.di.domainModule
import com.coders.chatapplication.presentation.di.presentationModule
import com.coders.chatapplication.presentation.sync.workers.SyncFriendshipsWorker
import com.coders.chatapplication.presentation.sync.workers.SyncMessagesWorker
import com.coders.chatapplication.presentation.sync.workers.SyncRoomsWorker
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber
import java.util.concurrent.TimeUnit

class MainApplication : Application() {
	override fun onCreate() {
		super.onCreate()
		startKoin {
			androidLogger(Level.DEBUG)
			androidContext(this@MainApplication)
			modules(
				listOf(
					presentationModule,
					dataModule,
					networkingModule,
					dbModule,
					domainModule
				)
			)
		}
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