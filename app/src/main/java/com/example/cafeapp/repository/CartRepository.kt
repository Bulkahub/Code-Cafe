package com.example.cafeapp.repository

import androidx.lifecycle.MutableLiveData
import com.example.cafeapp.dataclass.CartItem

// Объект-репозиторий для хранения состояния корзины в виде LiveData.
object CartRepository {
    // Живой список товаров в корзине, доступный для подписки и обновлений из ViewModel и фрагментов.
    val cartList = MutableLiveData<MutableList<CartItem>>(mutableListOf())

}