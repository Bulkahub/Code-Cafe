package com.example.cafeapp.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.cafeapp.R
import com.example.cafeapp.authmanager.AuthManager
import com.example.cafeapp.databinding.ActivityLoginScreenBinding
import com.example.cafeapp.repository.UsersRepository
import com.example.cafeapp.view.UserViewModel
import com.example.cafeapp.view.UserViewModelFactory

class LoginScreenActivity : AppCompatActivity() {

    private lateinit var viewModel: UserViewModel

    lateinit var textViewCreateAccount: TextView
    lateinit var binding: ActivityLoginScreenBinding
    private lateinit var authManager: AuthManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()



        //Инициализация привязки к макету .
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login_screen)


         authManager = AuthManager(applicationContext)


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
                val userId = viewModel.getUserId()
                if(userId != null){
                val userName = binding.loginUserName.text.toString()
                authManager.saveSession(userId, userName)

                val intent = Intent(this, MenuScreenActivity::class.java)
                intent.putExtra("username", userName)
                intent.putExtra("userId",userId)
                startActivity(intent)
                finish()
                }else{
                    Log.w("LoginFlow", "userId is null — возможно, ещё не успел обновиться")
                }
            } else {
                Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}


