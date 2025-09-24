package com.example.cafeapp.dataclass

import java.util.UUID

// Data class describing a notification in the system.
data class NotificationData(
    val id: String = UUID.randomUUID().toString(),
    val message: String,
    val timestamp: Long = System.currentTimeMillis(),
    val type: Type = Type.INFO,
    val isRead: Boolean = false
){
    // Enumeration of available notification types.
    enum class Type{
        INFO,SUCCESS,ERROR
    }
}
