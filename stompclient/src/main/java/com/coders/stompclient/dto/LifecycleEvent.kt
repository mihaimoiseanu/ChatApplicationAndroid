package com.coders.stompclient.dto

import java.util.*

class LifecycleEvent(
    val type: Type,
    val excetion:Exception? = null,
    val message:String? = null,
    var handshakeResponseHeaders:MutableMap<String,String?> = TreeMap()
) {

    enum class Type{
        OPENED, CLOSED, ERROR, FAILED_SERVER_HEARTBEAT
    }
}