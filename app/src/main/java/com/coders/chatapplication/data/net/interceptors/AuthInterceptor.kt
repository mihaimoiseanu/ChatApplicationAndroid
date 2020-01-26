package com.coders.chatapplication.data.net.interceptors

import android.text.TextUtils
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {

	var authToken:String? = null

	override fun intercept(chain: Interceptor.Chain): Response {

		val request = chain.request().newBuilder()

		if(!TextUtils.isEmpty(authToken)){
			request.addHeader("Authorization", "$authToken")
		}

		return chain.proceed(request.build())
	}
}