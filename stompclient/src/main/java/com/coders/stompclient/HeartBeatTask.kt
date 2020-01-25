package com.coders.stompclient

import com.coders.stompclient.dto.StompCommand
import com.coders.stompclient.dto.StompHeader
import com.coders.stompclient.dto.StompMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.math.max

class HeartBeatTask(
	private val sendClientHeartBeat: (String) -> Unit,
	private val onServerHeartBeatFailed: (() -> Unit)?,
	private val coroutineScope: CoroutineScope
) {

	private val logger: Logger = LoggerFactory.getLogger(HeartBeatTask::class.java.simpleName)

	var serverHeartbeat = 0L
	var clientHeartbeat = 0L

	private var serverHeartbeatNew = 0L
	private var clientHeartbeatNew = 0L

	private var lastServerHeartbeat = 0L

	private var clientSendHeartbeatJob: Job? = null
	private var serverCheckHeartbeatJob: Job? = null

	fun consumeHeartBeat(message: StompMessage): Boolean {
		when (message.stompCommand) {
			StompCommand.CONNECTED -> heartBeatHandshake(message.findHeader(StompHeader.HEART_BEAT))
			StompCommand.SEND -> abortClientHeartBeatSend()
			StompCommand.MESSAGE -> abortServerHeartBeatCheck()
			StompCommand.UNKNOWN -> {
				if ("\n" == message.payload) {
					abortServerHeartBeatCheck()
					return false
				}
			}
		}
		return true
	}

	fun shutdown() = coroutineScope.launch {
		clientSendHeartbeatJob?.cancelAndJoin()
		serverCheckHeartbeatJob?.cancelAndJoin()
		lastServerHeartbeat = 0L
	}

	/**
	 * Analise heart-beat sent from server (if any), to adjust the frequency.
	 * Startup the heart-beat logic.
	 */
	private fun heartBeatHandshake(heartBeatHeader: String?) {
		if (heartBeatHeader != null) { // The heart-beat header is OPTIONAL
			val heartbeats = heartBeatHeader.split(",").toTypedArray()
			if (clientHeartbeatNew > 0) { //there will be heart-beats every MAX(<cx>,<sy>) milliseconds
				clientHeartbeat = max(clientHeartbeatNew, heartbeats[1].toLong())
			}
			if (serverHeartbeatNew > 0) { //there will be heart-beats every MAX(<cx>,<sy>) milliseconds
				serverHeartbeat = serverHeartbeatNew.coerceAtLeast(heartbeats[0].toLong())
			}
		}
		if (clientHeartbeat > 0 || serverHeartbeat > 0) {

			if (clientHeartbeat > 0) { //client MUST/WANT send heart-beat
				scheduleClientHeartBeat()
			}
			if (serverHeartbeat > 0) {
				scheduleServerHeartBeatCheck()
				// initialize the server heartbeat
				lastServerHeartbeat = System.currentTimeMillis()
			}
		}
	}

	private fun scheduleServerHeartBeatCheck() {
		if (serverHeartbeat > 0) {
			serverCheckHeartbeatJob = coroutineScope.launch {
				delay(serverHeartbeat)
				checkServerHeartBeat()
			}
		}
	}

	private fun checkServerHeartBeat() {
		if (serverHeartbeat > 0) {
			logger.debug("check server heartbeat")
			val now = System.currentTimeMillis()
			val boundary = now - (3 * serverHeartbeat)
			if (lastServerHeartbeat < boundary) {
				onServerHeartBeatFailed?.invoke()
			} else {
				lastServerHeartbeat = System.currentTimeMillis()
			}
		}
	}

	private fun scheduleClientHeartBeat() {
		if (clientHeartbeat > 0) {
			clientSendHeartbeatJob = coroutineScope.launch {
				delay(clientHeartbeat)
				sendClientHeartBeat()
			}
		}
	}

	private fun sendClientHeartBeat() {
		logger.debug("Send client heart beat")
		sendClientHeartBeat("\r\n")
		scheduleClientHeartBeat()
	}

	private fun abortServerHeartBeatCheck() {
		lastServerHeartbeat = System.currentTimeMillis()
		coroutineScope.launch {
			serverCheckHeartbeatJob?.cancelAndJoin()
			scheduleServerHeartBeatCheck()

		}
	}

	private fun abortClientHeartBeatSend() {
		coroutineScope.launch {
			clientSendHeartbeatJob?.cancelAndJoin()
			scheduleClientHeartBeat()
		}
	}
}