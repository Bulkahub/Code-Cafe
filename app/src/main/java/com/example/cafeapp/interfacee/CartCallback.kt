package com.example.cafeapp.interfacee

// TODO: Not used yet. Planned for the user notification system.
// Will be triggered when the order status changes (e.g., "Order accepted", "Coffee is ready").
// May be used in a ViewModel or notification fragment.
interface OrderStatusCallback {
    fun onOrderStatusChanged()
}