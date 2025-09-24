package com.example.cafeapp.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cafeapp.repository.UsersRepository

/**
 * Factory for creating UserViewModel with a UsersRepository parameter.
 * Required because ViewModelProvider does not support constructors with arguments by default.
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