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


/** Репозиторий для управления данными корзины пользователя через DataStore.*/
class CartRepository @Inject constructor(private val dataStore: DataStore<Preferences>) {

    companion object {
        // Ключ для хранения списка товаров в корзине в формате JSON.
        val CART_KEY = stringPreferencesKey("cart_items")
    }

    // Поток корзины: считываем данные из DataStore и декодируем их из JSON в список объектов CartItem.
    val cartItems: Flow<List<CartItem>> = dataStore.data.map { preferences ->
        val json = preferences[CART_KEY] ?: "[]" // Если данных нет — возвращаем пустой список.
        Json.decodeFromString(ListSerializer(CartItem.serializer()), json)

    }

    // Сохраняем список товаров в корзине: сериализуем в JSON и записываем в DataStore.
    suspend fun saveCart(items: List<CartItem>) {
        val json = Json.encodeToString(items)
        dataStore.edit { prefs ->
            prefs[CART_KEY] = json
        }
    }

    // Очищаем корзину: удаляем соответствующий ключ из DataStore.
    suspend fun clearCart() {
        dataStore.edit { prefs ->
            prefs.remove(CART_KEY)
        }
    }
}