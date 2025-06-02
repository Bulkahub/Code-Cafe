package com.example.cafeapp.activity

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.cafeapp.R
import com.example.cafeapp.databinding.ActivityLoginScreenBinding
import com.example.cafeapp.repository.UsersRepository
import com.example.cafeapp.view.UserViewModel
import com.example.cafeapp.view.UserViewModelFactory

class LoginScreenActivity : AppCompatActivity() {

    private lateinit var viewModel: UserViewModel


    lateinit var textViewCreateAccount: TextView
    lateinit var binding: ActivityLoginScreenBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        //Инициализация привязки к макету .
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login_screen)


        val userRepository = UsersRepository(applicationContext)
        val factory = UserViewModelFactory(userRepository)


        viewModel = ViewModelProvider(this, factory)[UserViewModel::class.java]

        //Переход на экран создания аккаунта.
        textViewCreateAccount = findViewById(R.id.createAccountTextView)
        textViewCreateAccount.setOnClickListener {
            val intent = Intent(this, CreateAccountScreenActivity::class.java)
            startActivity(intent)
        }


        //Обработчик кнопки входа.
        binding.buttonLogin.setOnClickListener {
            viewModel.login(
                binding.loginUserName.text.toString().trim(),
                binding.loginPassword.text.toString().trim()
            )
        }

        //Следим за статусом авторизации.
        viewModel.loginStatus.observe(this) { success ->
            if (success) {
                val intent = Intent(this, MenuScreenActivity::class.java)
                intent.putExtra("username", binding.loginUserName.text.toString())
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}


