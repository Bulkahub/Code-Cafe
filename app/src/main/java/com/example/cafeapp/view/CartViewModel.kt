package com.example.cafeapp.view

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.cafeapp.dataclass.CartItem
import com.example.cafeapp.repository.CartRepository

/**
 * ViewModel для управления корзиной.
 * Использует CartRepository как источник данных и предоставляет методы для обновления корзины.
 */
class CartViewModel(application: Application) : AndroidViewModel(application) {

    // LiveData со списком товаров в корзине.
    val cartList: MutableLiveData<MutableList<CartItem>> get() = CartRepository.cartList

    //Добавляет новый товар в корзину и обновляет LiveData.
    fun addToCart(item: CartItem) {
        val updateList = CartRepository.cartList.value?.toMutableList() ?: mutableListOf()
        updateList.add(item)
        CartRepository.cartList.postValue(updateList)
    }

    fun addToFavorites(item: CartItem){

    }
}
