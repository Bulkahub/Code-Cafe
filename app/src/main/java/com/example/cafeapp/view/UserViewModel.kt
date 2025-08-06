package com.example.cafeapp.view


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cafeapp.authmanager.AuthManager
import com.example.cafeapp.repository.UsersRepository
import kotlin.math.PI

/**ViewModel следит за статусом входа
 * Связывает UI с UsersRepository и предоставляет LiveData для отслеживания**/
class UserViewModel(private val userRepository: UsersRepository) : ViewModel() {

    // Результат регистрации (true — успешно, false — ошибка).
    private val _registrationStatus = MutableLiveData<Boolean>()
    val registrationStatus: LiveData<Boolean> get() = _registrationStatus

    // Результат входа (true — успешно, false — ошибка).
    private val _loginStatus = MutableLiveData<Boolean>()
    val loginStatus: LiveData<Boolean> get() = _loginStatus

    // Сообщение об ошибке (если вход или регистрация завершились неудачно).
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    //Id текущего пользователя.Если вход выполнен успешно.
    var currentUserId: String? = null
        private set


    /**
     * Вызывает вход пользователя через репозиторий.
     * При неудаче устанавливает сообщение ошибки.
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

    //Возвращает Id текущего пользователя.
    fun getUserId(): String? {
        return currentUserId
    }

    /**
     * Регистрирует нового пользователя через репозиторий.
     * При неудаче сохраняет сообщение ошибки.
     */
    fun registerUser(userName: String, password: String) {
        userRepository.createAccount(userName, password) { success, message ->
            _registrationStatus.value = success
            if (!success) _errorMessage.value = message
        }
    }
}