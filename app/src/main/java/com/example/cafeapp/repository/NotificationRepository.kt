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

/** Репозиторий для управления уведомлениями внутри приложения.
 *  Хранит список NotificationData через StateFlow и позволяет добавлять, отложить и очищать уведомления.**/
@Singleton
class NotificationRepository @Inject constructor() {

    // Внутренний поток уведомлений — MutableStateFlow.
    private val _notification = MutableStateFlow<List<NotificationData>>(emptyList())

    // Публичный поток уведомлений для подписки во ViewModel/Fragment.
    val notification: StateFlow<List<NotificationData>> = _notification

    /**
     * Добавляет уведомление в список с указанным типом.
     * Тип по умолчанию — информационное сообщение (INFO).
     */
    fun push(message: String, type: NotificationData.Type = NotificationData.Type.INFO) {
        val note = NotificationData(message = message, type = type)
        _notification.update { it + note } // Добавляем к текущему списку.
    }

    /**
     * Добавляет уведомление с заданной задержкой (по умолчанию 10 секунд).
     * Выполняется в фоновом потоке через CoroutineScope.
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

    // Очищает список уведомлений.
    fun clear() {
        _notification.value = emptyList()
    }
}