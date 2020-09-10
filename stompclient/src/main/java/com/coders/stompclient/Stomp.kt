package com.coders.stompclient

import com.coders.stompclient.provider.ConnectionProvider
import com.coders.stompclient.provider.OkHttpConnectionProvider
import okhttp3.OkHttpClient

object Stomp {

	fun over(
		type: ConnectionProvider.Type,
		uri: String,
		headers: MutableMap<String, String> = mutableMapOf(),
		okHttpClient: OkHttpClient? = null,
		reconnect: Boolean = true
	): StompClient {

		val connectionProvider = when (type) {
			ConnectionProvider.Type.OKHTTP -> {
				OkHttpConnectionProvider(uri, headers, okHttpClient ?: OkHttpClient())
			}
		}
		return StompClient(connectionProvider).apply {
			clientHeartBeat = 10_000
			serverHeartBeat = 10_000
			this.reconnect = reconnect
		}
	}
}