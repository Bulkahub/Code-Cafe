package com.example.cafeapp.view


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cafeapp.repository.UsersRepository

/**ViewModel следит за статусом входа**/
class UserViewModel(private val userRepository: UsersRepository) : ViewModel() {

    private val _registrationStatus = MutableLiveData<Boolean>()
    val registrationStatus: LiveData<Boolean> get() = _registrationStatus

    private val _loginStatus = MutableLiveData<Boolean>()
    val loginStatus: LiveData<Boolean> get() = _loginStatus

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage


    fun login(userName: String, password: String) {
        userRepository.login(userName, password) { success, message ->
            _loginStatus.value = success
            if (!success) _errorMessage.value = message
        }
    }

    fun registerUser(userName: String, password: String) {
        userRepository.createAccount(userName, password) { success, message ->
            _registrationStatus.value = success
            if (!success) _errorMessage.value = message
        }
    }

}