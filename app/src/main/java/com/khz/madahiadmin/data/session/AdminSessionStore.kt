package com.khz.madahiadmin.data.session

import android.content.Context

class AdminSessionStore(context: Context) {
    private val preferences = context.getSharedPreferences(
        "madahi_admin_session",
        Context.MODE_PRIVATE
    )

    fun getToken(): String? = preferences.getString(
        KEY_TOKEN,
        null
    )

    fun saveToken(token: String) {
        preferences.edit()
            .putString(
                KEY_TOKEN,
                token
            )
            .apply()
    }

    fun clear() {
        preferences.edit()
            .remove(KEY_TOKEN)
            .apply()
    }

    private companion object {
        const val KEY_TOKEN = "admin_token"
    }
}
