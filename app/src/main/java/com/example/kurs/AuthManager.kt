package com.example.kurs

import android.content.Context
import android.content.SharedPreferences

object AuthManager {
    private const val PREFS_NAME = "auth_prefs"
    private const val KEY_TOKEN = "auth_token"
    private const val KEY_USER_ID = "user_id"

    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    var token: String?
        get() = prefs.getString(KEY_TOKEN, null)
        set(value) = prefs.edit().putString(KEY_TOKEN, value).apply()

    var userId: Int
        get() = prefs.getInt(KEY_USER_ID, -1)
        set(value) = prefs.edit().putInt(KEY_USER_ID, value).apply()

    fun isLoggedIn(): Boolean = token != null

    fun logout() {
        prefs.edit().clear().apply()
    }
}