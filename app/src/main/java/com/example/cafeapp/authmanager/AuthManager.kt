package com.example.cafeapp.authmanager

import android.content.Context
import android.util.Log

// User session manager.
class AuthManager(context: Context) {

    // Initialize SharedPreferences for storing session data.
    private val prefs = context.getSharedPreferences("CaffeAppPref", Context.MODE_PRIVATE)

    // Save user data in SharedPreferences.
    fun saveSession(userId: String, userName: String) {
        prefs.edit()
            .putString("loggedInUser", userId)
            .putString("username", userName)
            .apply()
    }

    // Check if the user is authorized.
    fun isLoggedIn(): Boolean {
        val userId = prefs.getString("loggedInUser", null)
        Log.d("SharedPrefsCheck", "Saved userId: $userId")
        return userId != null
    }

    // Retrieve the saved userId.
    fun getUserId(): String? = prefs.getString("loggedInUser", null)

    // Clear all session data (log out).
    fun clearSession() {
        prefs.edit().clear().apply()
    }
}