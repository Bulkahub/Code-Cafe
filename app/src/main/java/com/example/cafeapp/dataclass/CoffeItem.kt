package com.example.cafeapp.dataclass

//Класс данных,хранящий и предоставляющий  данные меню.
data class CoffeItem(
    val name: String,
    val milkType: String,
    val price: String,
    val imageRecId: Int,
    val description: String
)
