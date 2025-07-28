package com.example.cafeapp.hilt

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/** Класс Application, необходимый для инициализации Hilt при запуске приложения.*/
@HiltAndroidApp
class CafeAppHilt: Application() {
    // Дополнительная логика инициализации приложения может быть добавлена здесь при необходимости.
}