package com.example.cafeapp.view


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cafeapp.authmanager.AuthManager
import com.example.cafeapp.repository.UsersRepository
import kotlin.math.PI

/**ViewModel monitors login status.
 * Connects the UI with UsersRepository and provides LiveData for tracking.**/
class UserViewModel(private val userRepository: UsersRepository) : ViewModel() {

    // Registration result (true — success, false — failure).
    private val _registrationStatus = MutableLiveData<Boolean>()
    val registrationStatus: LiveData<Boolean> get() = _registrationStatus

    // Login result (true — success, false — failure).
    private val _loginStatus = MutableLiveData<Boolean>()
    val loginStatus: LiveData<Boolean> get() = _loginStatus

    // Error message (if login or registration failed).
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    // ID of the current user. Set if login is successful.
    var currentUserId: String? = null
        private set


    /**
     * Triggers user login via the repository.
     * Sets error message on failure.
     */
    fun login(userName: String, password: String) {
        userRepository.login(userName, password) { success, userId ->
            _loginStatus.value = success
            if (success && userId != null) {
                currentUserId = userId
                _loginStatus.value = true
            } else {
                _errorMessage.value = "Login Error"
                _loginStatus.value = false
            }
        }
    }

    // Returns the current user's ID.
    fun getUserId(): String? {
        return currentUserId
    }

    /**
     * Registers a new user via the repository.
     * Stores error message on failure.
     */
    fun registerUser(userName: String, password: String) {
        userRepository.createAccount(userName, password) { success, message ->
            _registrationStatus.value = success
            if (!success) _errorMessage.value = message
        }
    }
}