package com.example.cafeapp.authmanager

import android.content.Context
import android.util.Log

//Менеджер сессии пользователя.
class AuthManager(context: Context) {

    //Инициализация SharedPreferences для хранения данных сессии.
    private val prefs = context.getSharedPreferences("CaffeAppPref", Context.MODE_PRIVATE)

    //Сохраняет данные пользователя в SharedPreferences.
    fun saveSession(userId: String, userName: String) {
        prefs.edit()
            .putString("loggedInUser", userId)
            .putString("username", userName)
            .apply()
    }

    //Проверяет, авторизован ли пользователь
    fun isLoggedIn(): Boolean {
        val userId = prefs.getString("loggedInUser", null)
        Log.d("SharedPrefsCheck", "Saved userId: $userId")
        return userId != null
    }

    //Получает сохраненный userId.
    fun getUserId(): String? = prefs.getString("loggedInUser", null)

    //Очищает все данные сессии (выход из аккаунта).
    fun clearSession() {
        prefs.edit().clear().apply()
    }
}