package com.example.cafeapp.repository

import android.content.Context
import android.util.Log
import com.example.cafeapp.authmanager.AuthManager
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Репозиторий, отвечающий за регистрацию и авторизацию пользователей.
 * Хранит учетные данные в Firestore и сохраняет ID текущего пользователя в SharedPreferences.
 */
class UsersRepository(private val context: Context) {

    // Подключение к Firestore и локальному хранилищу настроек.
    private val firestore = FirebaseFirestore.getInstance()
    private val sharedPreferences =
        context.getSharedPreferences("CaffeAppPref", Context.MODE_PRIVATE)


    /**
     * Регистрирует нового пользователя:
     * - Проверяет валидность полей
     * - Генерирует уникальный userId
     * - Сохраняет данные пользователя в Firestore
     * - Локально сохраняет userId текущего пользователя
     *
     * @param userName имя пользователя
     * @param password пароль
     * @param callback результат операции (успех/ошибка + сообщение)
     */
    fun createAccount(userName: String, password: String, callback: (Boolean, String) -> Unit) {
        //Проверка,что поля не пустые и пароль достаточно длинный.
        if (userName.isBlank() || password.isBlank()) {
            callback(false, "Invalid input")
            return
        }
        if (password.length < 6) {
            callback(false, "Password must be at least 6 characters")
            return
        }

        // Генерация уникального ID и формирование данных пользователя.
        val userId = firestore.collection("Users").document().id//Генерируем id заранее.
        val userMap = hashMapOf(
            "userId" to userId,
            "userName" to userName.trim(),
            "password" to password.trim()
        )

        // Сохраняем пользователя в Firestore.
        firestore.collection("Users").document(userId)
            .set(userMap)//Используем set() с фиксированным id.
            .addOnSuccessListener {
                // Успешная регистрация — сохраняем ID в SharedPreferences.
                sharedPreferences.edit().putString("loggedInUser", userId).apply()
                callback(true, "Account created successfully")
            }
            .addOnFailureListener { e ->
                callback(false, "Error creating account")
            }
    }

    /**
     * Пытается выполнить вход по имени и паролю:
     * - Проверяет что поля не пустые
     * - Ищет пользователя по имени
     * - Сравнивает пароль
     * - Сохраняет ID в SharedPreferences при успехе
     *
     * @param userName имя пользователя
     * @param password пароль
     * @param callback результат (успех/ошибка + сообщение)
     */
    fun login(userName: String, password: String, callback: (Boolean, String?) -> Unit) {
        //Проверка что поля не пустые
        if (userName.isBlank() || password.isBlank()) {
            callback(false, null)
            return
        }
        firestore.collection("Users").whereEqualTo("userName", userName).limit(1).get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val user = documents.documents[0]
                    val storedPassword = user.getString("password") ?: ""
                    val userId = user.id

                    if (storedPassword == password) {
                        val authManager = AuthManager(context)
                        authManager.saveSession(userId, userName.trim())
                        callback(true, userId)
                    } else {
                        callback(false, null)
                    }
                } else {
                    callback(false, null)
                }
            }
            .addOnFailureListener { e ->
                callback(false, null)
            }
    }
}