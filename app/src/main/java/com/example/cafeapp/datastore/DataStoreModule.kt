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

/** Hilt module for providing DataStore and repositories.**/
@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    // Provides a DataStore with the file "favorites_store" for storing favorites.
    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create {
            context.preferencesDataStoreFile("favorites_store")
        }
    }

    // Creates a FavoritesRepository using the provided DataStore.
    @Provides
    @Singleton
    fun provideFavoritesRepository(
        dataStore: DataStore<Preferences>
    ): FavoritesRepository = FavoritesRepository(dataStore)


    // Provides a separate DataStore named "cart_store" — used for the cart.
    @Provides
    @Singleton
    @Named("cart")
    fun provideCartDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create {
            context.preferencesDataStoreFile("cart_store")
        }
    }

    // Creates a CartRepository by passing the DataStore bound via @Named("cart").
    @Provides
    @Singleton
    fun provideCartRepostory(@Named("cart") dataStore: DataStore<Preferences>): CartRepository {
        return CartRepository(dataStore)
    }
}
