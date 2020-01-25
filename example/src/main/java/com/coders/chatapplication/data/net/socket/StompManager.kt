package com.coders.chatapplication.data.net.socket

import com.coders.stompclient.Stomp
import com.coders.stompclient.provider.ConnectionProvider
import okhttp3.OkHttpClient

class StompManager(okHttpClient:OkHttpClient) {

	private val stomClient = Stomp.over(
		type = ConnectionProvider.Type.OKHTTP,
		uri = "",
		okHttpClient = okHttpClient
	)


}