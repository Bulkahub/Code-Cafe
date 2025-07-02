package com.example.cafeapp.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cafeapp.repository.UsersRepository

/**
 * Фабрика для создания UserViewModel с параметром UsersRepository.
 * Необходима, т.к. ViewModelProvider по умолчанию не поддерживает конструкторы с аргументами.
 */
class UserViewModelFactory(private val userRepository: UsersRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}