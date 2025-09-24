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

/** Repository for managing the user's favorites list using DataStore.*/
class FavoritesRepository @Inject constructor(private val dataStore: DataStore<Preferences>) {

    // JSON serializer for storing objects.
    private val gson = Gson()

    // Key for saving the list of favorite items in DataStore.
    private val FAVORITES_KEY = stringPreferencesKey("favorites_key")

    // Saves the list of favorite items to DataStore in JSON format.
    suspend fun saveFavorites(list: List<CartItem>) {
        val json = gson.toJson(list)
        dataStore.edit { prefs ->
            prefs[FAVORITES_KEY] = json
        }
    }

    // Loads the list of favorite items from DataStore.
    // Returns an empty list if no data is found.
    suspend fun loadFavorites(): List<CartItem> {
        val json = dataStore.data
            .map { it[FAVORITES_KEY] ?: "[]" }
            .first()
        return gson.fromJson(json, object : TypeToken<List<CartItem>>() {}.type)
    }
}