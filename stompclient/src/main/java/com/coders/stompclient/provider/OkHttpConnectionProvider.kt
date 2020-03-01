package com.coders.stompclient.provider

import com.coders.stompclient.dto.LifecycleEvent
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import org.slf4j.Logger
import org.slf4j.LoggerFactory


class OkHttpConnectionProvider(
	private val uri: String,
	private val connectHttpHeaders: Map<String, String> = mapOf(),
	private val okHttpClient: OkHttpClient
) : ConnectionProvider() {

	private val logger: Logger =
		LoggerFactory.getLogger(OkHttpConnectionProvider::class.java.simpleName)

	private var openSocket: WebSocket? = null

	override fun createWebSocketConnection() {
		super.createWebSocketConnection()
		launch {
			val requestBuilder = Request.Builder().url(uri)
			connectHttpHeaders.forEach { (key, value) ->
				requestBuilder.addHeader(key, value)
			}

			openSocket =
				okHttpClient.newWebSocket(requestBuilder.build(), object : WebSocketListener() {

					override fun onOpen(webSocket: WebSocket, response: Response) {
						logger.debug("WebSocket opened")
						val event = LifecycleEvent(LifecycleEvent.Type.OPENED)
						val headersAsMap = mutableMapOf<String, String?>()
						val headers = response.headers
						headers.names().forEach {
							headersAsMap[it] = headers[it]
						}
						event.handshakeResponseHeaders = headersAsMap
						handleLifecycle(event)
					}

					override fun onMessage(webSocket: WebSocket, text: String) {
						logger.debug("message received: $text")
						handleMessage(text)
					}

					override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
						logger.debug("message received: ${bytes.utf8()}")
						handleMessage(bytes.utf8())
					}

					override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
						openSocket = null
						logger.debug("WebSocket closed: $code, $reason")
						val event = LifecycleEvent(LifecycleEvent.Type.CLOSED)
						handleLifecycle(event)
					}

					override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
						webSocket.close(code, reason)
					}

					override fun onFailure(
						webSocket: WebSocket,
						t: Throwable,
						response: Response?
					) {
						logger.error("WebSocket error: ${t.localizedMessage} $response")
						openSocket = null
						launch {
							lifecycleChannel.send(
								LifecycleEvent(
									LifecycleEvent.Type.ERROR,
									Exception(t)
								)
							)
						}
						handleLifecycle(LifecycleEvent(LifecycleEvent.Type.CLOSED))
					}
				})
		}
	}

	override fun sendMessage(message: String) {
		if (!isConnected) {
			return
		}
		launch {
			openSocket?.send(message)
		}
	}

	private fun handleLifecycle(lifecycleEvent: LifecycleEvent) {
		launch { lifecycleChannel.send(lifecycleEvent) }
	}

	private fun handleMessage(message: String) {
		launch { messageChannel.send(message) }
	}

	override fun disconnect() {
		if (!isConnected) {
			return
		}
		launch {
			openSocket?.close(1000, "")
			messageChannel.close()
			lifecycleChannel.cancel()
			parentJob.complete()
		}
	}

	override val isConnected: Boolean
		get() = openSocket != null
}