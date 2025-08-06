package com.example.cafeapp.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.cafeapp.R
import com.example.cafeapp.databinding.ActivityCreateAccountScreenBinding
import com.example.cafeapp.repository.UsersRepository
import com.example.cafeapp.view.UserViewModel
import com.example.cafeapp.view.UserViewModelFactory

class CreateAccountScreenActivity : AppCompatActivity() {

    private lateinit var viewModel: UserViewModel

    lateinit var binding: ActivityCreateAccountScreenBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        //Инициализация привязки к макету.
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_account_screen)

        val userRepository = UsersRepository(applicationContext)
        val factory = UserViewModelFactory(userRepository)


        viewModel = ViewModelProvider(this, factory)[UserViewModel::class.java]

        //Обработчик кнопки "Создать аккаунт".
        binding.buttonCreateAccount.setOnClickListener {
            val userName = binding.createName.text.toString()
            val password = binding.createPassword.text.toString()

            //Проверяем,что все поля не пустые.
            if (userName.isNotEmpty() && password.isNotEmpty()) {
                viewModel.registerUser(userName, password)//Регистрируем пользователя.
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.registrationStatus.observe(this) { success ->
            if (success) {
                startActivity(Intent(this, LoginScreenActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Registration Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}