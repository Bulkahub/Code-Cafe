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
 * ViewModel for managing cart and favorite items.
 * Uses CartRepository and FavoritesRepository to store and update data via DataStore.
 */
@HiltViewModel
class CartViewModel @Inject constructor(
    private val favoritesRepository: FavoritesRepository,
    private val cartRepository: CartRepository
) :
    ViewModel() {

    // Flow of cart items retrieved from the repository. Wrapped in StateFlow for subscription.
    val _cartItems: StateFlow<List<CartItem>> = cartRepository.cartItems
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems

    // Calculates the number of items in the cart.
    val cartSize: StateFlow<Int> = cartItems.map { it.size }
        .stateIn(viewModelScope, SharingStarted.Eagerly, 0)


    /**
     * Adds an item to the cart: loads the current list, adds the new item, and saves it.
     * Uses suspend because first() is a blocking call.
     */
    suspend fun addToCart(item: CartItem) {
        val currentItems = cartRepository.cartItems.first()
        val updated = currentItems.toMutableList().apply { add(item) }
        cartRepository.saveCart(updated)

    }

    // Clears the cart by removing all items from DataStore.
    fun clearCart() {
        viewModelScope.launch {
            cartRepository.clearCart()
        }
    }


    // Internal flow of favorite items.
    private val _favoriteList = MutableStateFlow<List<CartItem>>(emptyList())
    val favoriteList: StateFlow<List<CartItem>> = _favoriteList

    // Calculates the number of favorite items.
    val favoriteCount: StateFlow<Int> = favoriteList.map { it.size }
        .stateIn(viewModelScope, SharingStarted.Eagerly, 0)

    // On ViewModel initialization — load favorites from DataStore.
    init {
        viewModelScope.launch {
            _favoriteList.value = favoritesRepository.loadFavorites() as MutableList<CartItem>
        }
    }

    /**
     * Adds an item to favorites if it's not already present.
     * Updates the local list and saves it to DataStore.
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

    /**Removes an item from favorites and saves the changes.*/
    fun removeFromFavorites(item: CartItem) {
        viewModelScope.launch {
            val update = _favoriteList.value.toMutableList()
            update.remove(item)
            _favoriteList.value = update
            favoritesRepository.saveFavorites(update)
        }
    }

    /** Removes a specific item from the cart by its ID.*/
    fun removeCart(item: CartItem) {
        viewModelScope.launch {
            val update = cartItems.value.toMutableList().apply {
                removeAll { it.id == item.id }
            }
            cartRepository.saveCart(update)
        }
    }
}
