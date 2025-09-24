package com.example.cafeapp.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cafeapp.dataclass.NotificationData
import com.example.cafeapp.repository.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


/** ViewModel for managing notification logic.*/

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val repo: NotificationRepository
) : ViewModel() {

    /**
     * Flow of all notifications.
     * Retrieved directly from the repository.
     */
    val notifications: StateFlow<List<NotificationData>> = repo.notification


    /**
     * Flow of unread notification count.
     * Updates dynamically when the list changes.
     */
    val unreadCount: StateFlow<Int> = notifications.map { list ->
        list.count { !it.isRead }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, 0)


    /**
     * Sends a notification immediately.
     * Default type is informational.
     */
    fun notifyNow(message: String, type: NotificationData.Type = NotificationData.Type.INFO) {
        repo.push(message, type)
    }


    /**
     * Sends a notification with a delay.
     * delayMillis — delay time (default is 10 seconds).
     */
    fun notifyLater(
        message: String,
        delayMillis: Long = 10000,
        type: NotificationData.Type = NotificationData.Type.INFO
    ) {
        repo.push(message = message, delayMillis, type)
    }


    /**
     * Clears all notifications.
     * Useful when logging out or resetting state.
     */
    fun clearAll() {
        repo.clear()
    }
}