package com.coders.stompclient.pathmatcher

import com.coders.stompclient.dto.StompHeader
import com.coders.stompclient.dto.StompMessage

class SimplePathMatcher : PathMatcher {

    override fun matches(path: String, message: StompMessage): Boolean {
        return message.findHeader(StompHeader.DESTINATION)?.let {
            path == it
        } ?: false
    }

}