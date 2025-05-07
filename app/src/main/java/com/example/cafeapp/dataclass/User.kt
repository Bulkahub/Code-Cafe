package com.example.cafeapp.dataclass

data class User(
    val uid: String = "",
    val userName: String = "",
    val email: String = "",
    val password: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
