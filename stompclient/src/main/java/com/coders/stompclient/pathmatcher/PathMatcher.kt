package com.coders.stompclient.pathmatcher

import com.coders.stompclient.dto.StompMessage

interface PathMatcher {
    fun matches(path: String, message: StompMessage): Boolean
}