package com.example.cafeapp.dataclass

// User model class used to store data about a registered user.
data class User(
    val uid: String = "",
    val userName: String = "",
    val email: String = "",
    val password: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
