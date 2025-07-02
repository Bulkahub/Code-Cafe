package com.example.cafeapp.dataclass

//Класс данных,хранящий и предоставляющий данные в корзине.
data class CartItem(
    val name: String,
    val size: String,
    val price: String,
    val image: Int
)
