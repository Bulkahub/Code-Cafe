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


/** ViewModel для управления логикой уведомлений.*/

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val repo: NotificationRepository
): ViewModel() {

    /**
     * Поток всех уведомлений.
     * Получаем напрямую из репозитория.
     */
    val notifications: StateFlow<List<NotificationData>> = repo.notification


    /**
     * Поток количества непрочитанных уведомлений.
     * Обновляется динамически при изменении списка.
     */
    val unreadCount: StateFlow<Int> = notifications.map { list ->
        list.count{!it.isRead}
    }.stateIn(viewModelScope, SharingStarted.Eagerly,0)


    /**
     * Отправить уведомление немедленно.
     * Тип по умолчанию — информационное сообщение.
     */
    fun notifyNow(message: String,type: NotificationData.Type = NotificationData.Type.INFO){
        repo.push(message,type)
    }


    /**
     * Отправить уведомление с задержкой.
     * delayMillis — время задержки (по умолчанию 10 секунд).
     */
    fun notifyLater(message: String,delayMillis: Long = 10000,type: NotificationData.Type = NotificationData.Type.INFO){
        repo.push(message = message,delayMillis,type)
    }


    /**
     * Удалить все уведомления.
     * Полезно при выходе из аккаунта или обновлении состояния.
     */
    fun clearAll(){
        repo.clear()
    }
}