package com.example.cafeapp.dataclass

import java.util.UUID

// Класс данных, описывающий уведомление в системе.
data class NotificationData(
    val id: String = UUID.randomUUID().toString(),
    val message: String,
    val timestamp: Long = System.currentTimeMillis(),
    val type: Type = Type.INFO,
    val isRead: Boolean = false
){
    // Перечисление доступных типов уведомлений.
    enum class Type{
        INFO,SUCCESS,ERROR
    }
}
