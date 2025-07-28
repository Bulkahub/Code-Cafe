package com.example.cafeapp.view


import android.util.Log
import androidx.core.view.OneShotPreDrawListener.add
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cafeapp.dataclass.CartItem
import com.example.cafeapp.repository.CartRepository
import com.example.cafeapp.repository.FavoritesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel для управления корзиной и избранными товарами.
 * Использует CartRepository и FavoritesRepository для хранения и обновления данных через DataStore.
 */
@HiltViewModel
class CartViewModel @Inject constructor(
    private val favoritesRepository: FavoritesRepository,
    private val cartRepository: CartRepository
) :
    ViewModel() {

    // Поток товаров в корзине, полученный из репозитория. Оборачиваем в StateFlow для подписки.
    val _cartItems: StateFlow<List<CartItem>> = cartRepository.cartItems
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems

    // Подсчитываем количество элементов в корзине.
    val cartSize: StateFlow<Int> = cartItems.map { it.size }
        .stateIn(viewModelScope, SharingStarted.Eagerly, 0)


    /**
     * Добавляет товар в корзину: загружает текущий список, добавляет новый, сохраняет.
     * Использует suspend, так как first() — это блокирующий вызов.
     */
    suspend fun addToCart(item: CartItem) {
        val currentItems = cartRepository.cartItems.first()
        val updated = currentItems.toMutableList().apply { add(item) }
        cartRepository.saveCart(updated)

    }

    //Очищает корзину, удаляя все элементы из DataStore.
    fun clearCart() {
        viewModelScope.launch {
            cartRepository.clearCart()
        }
    }


    // Внутренний поток избранных элементов.
    private val _favoriteList = MutableStateFlow<List<CartItem>>(emptyList())
    val favoriteList: StateFlow<List<CartItem>> = _favoriteList

    // Подсчитываем количество избранных товаров.
    val favoriteCount: StateFlow<Int> = favoriteList.map { it.size }
        .stateIn(viewModelScope, SharingStarted.Eagerly, 0)

    // При инициализации ViewModel — загружаем избранные из DataStore.
    init {
        viewModelScope.launch {
            _favoriteList.value = favoritesRepository.loadFavorites() as MutableList<CartItem>
        }
    }

    /**
     * Добавляет товар в избранное, если его ещё нет.
     * Обновляет локальный список и сохраняет в DataStore.
     */
    fun addToFavorites(item: CartItem) {
        viewModelScope.launch {
            val update = _favoriteList.value.toMutableList()
            if (item !in update) {
                update.add(item)
                _favoriteList.value = update
                favoritesRepository.saveFavorites(update)
            }
        }
    }

    /**Удаляет товар из избранного и сохраняет изменения.*/
    fun removeFromFavorites(item: CartItem) {
        viewModelScope.launch {
            val update = _favoriteList.value.toMutableList()
            update.remove(item)
            _favoriteList.value = update
            favoritesRepository.saveFavorites(update)
        }
    }

    /**Удаляет конкретный товар из корзины по его ID.*/
    fun removeCart(item: CartItem) {
        viewModelScope.launch {
            val update = cartItems.value.toMutableList().apply {
                removeAll { it.id == item.id }
            }
            cartRepository.saveCart(update)
        }
    }
}
