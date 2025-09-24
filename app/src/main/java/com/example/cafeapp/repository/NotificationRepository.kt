package com.example.cafeapp.repository

import com.example.cafeapp.dataclass.NotificationData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/** Repository for managing in-app notifications.
 *  Stores a list of NotificationData via StateFlow and allows adding, delaying, and clearing notifications.**/
@Singleton
class NotificationRepository @Inject constructor() {

    // Internal notification flow — MutableStateFlow.
    private val _notification = MutableStateFlow<List<NotificationData>>(emptyList())

    // Public notification flow for subscription in ViewModel/Fragment.
    val notification: StateFlow<List<NotificationData>> = _notification

    /**
     * Adds a notification to the list with the specified type.
     * Default type — informational message (INFO).
     */
    fun push(message: String, type: NotificationData.Type = NotificationData.Type.INFO) {
        val note = NotificationData(message = message, type = type)
        _notification.update { it + note } // Append to the current list.
    }

    /**
     * Adds a notification with a specified delay (default is 10 seconds).
     * Executed on a background thread via CoroutineScope.
     */
    fun push(
        message: String,
        delayMillis: Long = 10000,
        type: NotificationData.Type = NotificationData.Type.INFO
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            delay(delayMillis)
            push(message, type)
        }
    }

    // Clears the notification list.
    fun clear() {
        _notification.value = emptyList()
    }
}