package com.example.cafeapp.dataclass


import kotlinx.serialization.Serializable
import java.util.UUID

// Data class that stores and provides cart item information.
@Serializable
data class CartItem(
    val name: String,
    val size: String,
    val price: String,
    val image: Int,
    val quantity: Int,
    val id: String = UUID.randomUUID().toString()
)
