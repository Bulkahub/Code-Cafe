package com.example.cafeapp.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.cafeapp.dataclass.CartItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/** Репозиторий для управления списком избранного пользователя с использованием DataStore.*/
class FavoritesRepository @Inject constructor(private val dataStore: DataStore<Preferences>) {

    // JSON-сериализатор для хранения объектов.
    private val gson = Gson()

    // Ключ для сохранения списка избранных товаров в DataStore.
    private val FAVORITES_KEY = stringPreferencesKey("favorites_key")

    //Сохраняет список избранных товаров в DataStore в формате JSON.
    suspend fun saveFavorites(list: List<CartItem>) {
        val json = gson.toJson(list)
        dataStore.edit { prefs ->
            prefs[FAVORITES_KEY] = json
        }
    }

    //Загружает список избранных товаров из DataStore.
    // Если данные отсутствуют — возвращает пустой список.
    suspend fun loadFavorites(): List<CartItem> {
        val json = dataStore.data
            .map { it[FAVORITES_KEY] ?: "[]" }
            .first()
        return gson.fromJson(json, object : TypeToken<List<CartItem>>() {}.type)
    }
}