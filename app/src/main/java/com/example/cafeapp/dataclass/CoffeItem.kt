package com.example.cafeapp.dataclass

// Data class that stores and provides menu item information.
data class CoffeItem(
    val name: String,
    val milkType: String,
    val price: String,
    val imageRecId: Int,
    val description: String
)
