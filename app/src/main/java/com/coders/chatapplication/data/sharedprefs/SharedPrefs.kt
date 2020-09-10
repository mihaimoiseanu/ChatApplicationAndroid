package com.coders.chatapplication.data.sharedprefs

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPrefs
@Inject constructor(@ApplicationContext context: Context) {
    private val sharedPreferences = context.getSharedPreferences("chat", Context.MODE_PRIVATE)

    var userId: Long
        get() = sharedPreferences.getLong("USER_ID", -1)
        set(value) {
            sharedPreferences.edit().putLong("USER_ID", value).apply()
        }

    var userEmail: String
        get() = sharedPreferences.getString("USER_EMAIL", "")!!
        set(value) {
            sharedPreferences.edit().putString("USER_EMAIL", value).apply()
        }

    var authToken: String
        get() = sharedPreferences.getString("AUTH_TOKEN", "")!!
        set(value) {
            sharedPreferences.edit().putString("AUTH_TOKEN", value).apply()
        }
}