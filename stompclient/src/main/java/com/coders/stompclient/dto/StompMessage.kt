package com.coders.stompclient.dto

import java.io.StringReader
import java.util.*
import java.util.regex.Pattern

class StompMessage(
    val stompCommand: String,
    val stompHeaders: List<StompHeader>?,
    val payload: String?
) {

    fun findHeader(key: String): String? =
        stompHeaders?.find { it.key == key }?.value

    fun compile(legacyWhitespace: Boolean = false): String = buildString {
        append(stompCommand).append('\n')
        stompHeaders?.forEach {
            append(it.key).append(':').append(it.value).append('\n')
        }
        append('\n')
        payload?.also {
            append(it)
            if (legacyWhitespace) append("\n\n")
        }
        append(TERMINATE_MESSAGE_SYMBOL)
    }

    override fun toString(): String {
        return "StompMessage{" +
                "command='${stompCommand}'" +
                ", headers=${stompHeaders}" +
                ", payload='$payload'"
    }

    companion object {
        const val TERMINATE_MESSAGE_SYMBOL = "\u0000"
        private val PATTERN_HEADER = Pattern.compile("([^:\\s]+)\\s*:\\s*([^:\\s]+)")

        fun from(data:String?): StompMessage {
            if (data==null || data.trim().isEmpty()){
                return StompMessage(
                    StompCommand.UNKNOWN,
                    null,
                    data
                )
            }
            val reader = Scanner(StringReader(data))
            reader.useDelimiter("\\n")
            val command = reader.next()
            val headers = mutableListOf<StompHeader>()

            while(reader.hasNext(PATTERN_HEADER)){
                val matcher = PATTERN_HEADER.matcher(reader.next())
                matcher.find()
                headers.add(
                    StompHeader(
                        matcher.group(
                            1
                        ), matcher.group(2)
                    )
                )
            }

            reader.skip("\n\n")

            reader.useDelimiter(TERMINATE_MESSAGE_SYMBOL)
            val payload = if (reader.hasNext()) reader.next() else null
            return StompMessage(
                command,
                headers,
                payload
            )
        }
    }

}