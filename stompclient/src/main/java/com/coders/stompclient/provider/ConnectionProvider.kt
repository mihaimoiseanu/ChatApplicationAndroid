package com.coders.stompclient.provider

import com.coders.stompclient.dto.LifecycleEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlin.coroutines.CoroutineContext

abstract class ConnectionProvider : CoroutineScope {


	protected val parentJob = SupervisorJob()
	override val coroutineContext: CoroutineContext
		get() = Dispatchers.IO + parentJob

	val lifecycleChannel = Channel<LifecycleEvent>(Channel.UNLIMITED)
	val messageChannel = Channel<String>(Channel.UNLIMITED)

	abstract val isConnected: Boolean

	abstract fun createWebSocketConnection()
	abstract fun sendMessage(message: String)
	abstract fun disconnect()

	enum class Type {
		OKHTTP
	}
}