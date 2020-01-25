package com.coders.stompclient.dto

class StompHeader(
    val key:String,
    val value:String
) {

    override fun toString(): String {
        return "StompHeader{$key=$value}"
    }

    companion object{
        const val VERSION = "accept-version"
        const val HEART_BEAT = "heart-beat"
        const val DESTINATION = "destination"
        const val CONTENT_TYPE = "content-type"
        const val MESSAGE_ID = "message-id"
        const val ID = "id"
        const val ACK = "ack"
    }
}
