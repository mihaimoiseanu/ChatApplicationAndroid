package com.coders.chatapplication

import android.app.Application
import com.coders.chatapplication.data.di.dataModule
import com.coders.chatapplication.data.di.dbModule
import com.coders.chatapplication.data.di.networkingModule
import com.coders.chatapplication.domain.di.domainModule
import com.coders.chatapplication.presentation.di.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

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
}