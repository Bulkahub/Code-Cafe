package com.example.cafeapp.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.cafeapp.dataclass.CartItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import javax.inject.Inject


/** Repository for managing user's cart data via DataStore.*/
class CartRepository @Inject constructor(private val dataStore: DataStore<Preferences>) {

    companion object {
        // Key for storing the cart item list in JSON format.
        val CART_KEY = stringPreferencesKey("cart_items")
    }

    // Cart flow: read data from DataStore and decode it from JSON into a list of CartItem objects.
    val cartItems: Flow<List<CartItem>> = dataStore.data.map { preferences ->
        val json = preferences[CART_KEY] ?: "[]" // If no data — return an empty list.
        Json.decodeFromString(ListSerializer(CartItem.serializer()), json)

    }

    // Save the list of cart items: serialize to JSON and write to DataStore.
    suspend fun saveCart(items: List<CartItem>) {
        val json = Json.encodeToString(items)
        dataStore.edit { prefs ->
            prefs[CART_KEY] = json
        }
    }

    // Clear the cart: remove the corresponding key from DataStore.
    suspend fun clearCart() {
        dataStore.edit { prefs ->
            prefs.remove(CART_KEY)
        }
    }
}
