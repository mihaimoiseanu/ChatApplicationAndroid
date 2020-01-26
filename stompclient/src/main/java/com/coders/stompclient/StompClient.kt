package com.coders.stompclient

import com.coders.stompclient.dto.LifecycleEvent
import com.coders.stompclient.dto.StompCommand
import com.coders.stompclient.dto.StompHeader
import com.coders.stompclient.dto.StompMessage
import com.coders.stompclient.dto.StompMessage.Companion.from
import com.coders.stompclient.pathmatcher.PathMatcher
import com.coders.stompclient.pathmatcher.SimplePathMatcher
import com.coders.stompclient.provider.ConnectionProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import kotlin.coroutines.CoroutineContext


@FlowPreview
@ExperimentalCoroutinesApi
class StompClient(
	private val connectionProvider: ConnectionProvider,
	private val pathMatcher: PathMatcher = SimplePathMatcher()
) : CoroutineScope {

	private val logger: Logger =
		LoggerFactory.getLogger(StompClient::class.java.simpleName)

	private val parentJob = SupervisorJob()
	override val coroutineContext: CoroutineContext
		get() = Dispatchers.IO + parentJob

	var legacyWhitespace = false
	private var headers: List<StompHeader>? = null

	private var messageChannel = Channel<StompMessage>(Channel.UNLIMITED)
	var lifecycleChannel = Channel<LifecycleEvent>(Channel.UNLIMITED)
	var connectionChannel = Channel<Boolean>()

	private var channelsMap = ConcurrentHashMap<String, Channel<StompMessage>>()
	private var topicsMap = ConcurrentHashMap<String, String>()

	private val heartBeatTask = HeartBeatTask(::sendHeartBeat, {
		launch {
			lifecycleChannel.send(LifecycleEvent(LifecycleEvent.Type.FAILED_SERVER_HEARTBEAT))
		}
	}, this)

	var clientHeartBeat: Long
		get() = heartBeatTask.clientHeartbeat
		set(value) {
			heartBeatTask.clientHeartbeat = value
		}
	var serverHeartBeat: Long
		get() = heartBeatTask.serverHeartbeat
		set(value) {
			heartBeatTask.serverHeartbeat = value
		}

	fun connect(headers: MutableList<StompHeader> = mutableListOf()) {
		this.headers = headers
		if (connectionProvider.isConnected) {
			return
		}
		messageChannel = Channel()
		lifecycleChannel = Channel()
		connectionChannel = Channel()

		connectionProvider.lifecycleChannel.consumeAsFlow().onEach {
			when (it.type) {
				LifecycleEvent.Type.OPENED -> {
					headers.add(StompHeader(StompHeader.VERSION, SUPPORTED_VERSIONS))
					headers.add(
						StompHeader(
							StompHeader.HEART_BEAT,
							"${heartBeatTask.clientHeartbeat},${heartBeatTask.serverHeartbeat}"
						)
					)
					connectionProvider.sendMessage(
						StompMessage(
							StompCommand.CONNECT,
							headers,
							null
						).compile(legacyWhitespace)
					)
					launch { lifecycleChannel.send(it) }
				}
				LifecycleEvent.Type.CLOSED -> {
					disconnect()
				}
				LifecycleEvent.Type.ERROR -> {
					launch { lifecycleChannel.send(it) }
				}
			}
		}.launchIn(this)

		connectionProvider.messageChannel.consumeAsFlow()
			.map { from(it) }
			.filter { heartBeatTask.consumeHeartBeat(it) }
			.onEach {
				if (StompCommand.CONNECTED == it.stompCommand) {
					launch { connectionChannel.send(true) }
				} else {
					launch { messageChannel.send(it) }
				}
			}.launchIn(this)

		connectionProvider.createWebSocketConnection()
	}

	fun disconnect() {
		launch {
			logger.debug("Disconnect")
			heartBeatTask.shutdown()
			lifecycleChannel.close()
			messageChannel.close()
			connectionProvider.disconnect()
		}
	}

	fun send(destination: String, data: String? = null) {
		send(
			StompMessage(
				StompCommand.SEND,
				listOf(StompHeader(StompHeader.DESTINATION, destination)),
				data
			)
		)
	}

	fun subscribePath(
		destinationPath: String,
		headers: MutableList<StompHeader> = mutableListOf()
	): Channel<StompMessage> {
		val topicId = UUID.randomUUID().toString()

		if (channelsMap.contains(destinationPath)) {
			return channelsMap[destinationPath]!!
		}
		headers.add(StompHeader(StompHeader.ID, topicId))
		headers.add(StompHeader(StompHeader.DESTINATION, destinationPath))
		headers.add(StompHeader(StompHeader.ACK, DEFAULT_ACK))
		send(
			StompMessage(
				StompCommand.SUBSCRIBE,
				headers, null
			)
		)
		val channel = Channel<StompMessage>()
		topicsMap[destinationPath] = topicId
		channelsMap[destinationPath] = channel
		messageChannel
			.consumeAsFlow()
			.filter { pathMatcher.matches(destinationPath, it) }
			.onEach { launch { channelsMap[destinationPath]?.send(it) } }
			.launchIn(this)
		return channel
	}

	fun unsubscribePath(destinationPath: String) {
		val topicId = topicsMap.remove(destinationPath) ?: return
		channelsMap.remove(destinationPath)?.close()
		send(
			StompMessage(
				StompCommand.UNSUBSCRIBE,
				listOf(StompHeader(StompHeader.ID, topicId)), null
			)
		)
	}

	private fun send(stompMessage: StompMessage) {
		connectionProvider.sendMessage(stompMessage.compile(legacyWhitespace))
	}

	private fun sendHeartBeat(pingMessage: String) {
		if (connectionProvider.isConnected) {
			connectionProvider.sendMessage(pingMessage)
		}
	}

	companion object {
		const val SUPPORTED_VERSIONS = "1.1,1.2"
		const val DEFAULT_ACK = "auto"
	}
}