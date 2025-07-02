package com.example.cafeapp.interfacee

// TODO: Пока не используется. Планируется для системы уведомлений пользователю.
// при изменении статуса заказа (например, "Заказ принят", "Кофе готов")
// Может применяться во ViewModel или фрагменте уведомлений
interface OrderStatusCallback {
    fun onOrderStatusChanged()
}