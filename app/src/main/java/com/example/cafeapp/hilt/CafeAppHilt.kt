package com.example.cafeapp.hilt

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/** Application class required for initializing Hilt at app startup.*/
@HiltAndroidApp
class CafeAppHilt : Application() {
    // Additional application initialization logic can be added here if needed!
}