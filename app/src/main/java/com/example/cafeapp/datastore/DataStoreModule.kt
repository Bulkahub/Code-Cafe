package com.example.cafeapp.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.example.cafeapp.repository.CartRepository
import com.example.cafeapp.repository.FavoritesRepository
import javax.inject.Named
import javax.inject.Singleton

/** Модуль Hilt для предоставления DataStore и репозиториев.**/
@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    // Предоставляет DataStore с файлом "favorites_store" для хранения избранного.
    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create {
            context.preferencesDataStoreFile("favorites_store")
        }
    }

    // Создаёт FavoritesRepository, используя переданный DataStore.
    @Provides
    @Singleton
    fun provideFavoritesRepository(
        dataStore: DataStore<Preferences>
    ): FavoritesRepository = FavoritesRepository(dataStore)


    // Предоставляет отдельный DataStore с именем "cart_store" — используется для корзины.
    @Provides
    @Singleton
    @Named("cart")
    fun provideCartDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create {
            context.preferencesDataStoreFile("cart_store")
        }
    }

    // Создаёт CartRepository, передавая ему DataStore, привязанный через @Named("cart").
    @Provides
    @Singleton
    fun provideCartRepostory(@Named("cart") dataStore: DataStore<Preferences>): CartRepository {
        return CartRepository(dataStore)
    }
}
