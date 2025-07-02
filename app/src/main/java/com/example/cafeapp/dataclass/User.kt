package com.example.cafeapp.dataclass

// Класс модели пользователя, используемый для хранения данных о зарегистрированном юзере.
data class User(
    val uid: String = "",
    val userName: String = "",
    val email: String = "",
    val password: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
