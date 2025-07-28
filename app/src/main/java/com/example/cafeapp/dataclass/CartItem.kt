package com.example.cafeapp.dataclass


import kotlinx.serialization.Serializable
import java.util.UUID

//Класс данных,хранящий и предоставляющий данные в корзине.
@Serializable
data class CartItem(
    val name: String,
    val size: String,
    val price: String,
    val image: Int,
    val quantity: Int,
    val id: String = UUID.randomUUID().toString()
)
