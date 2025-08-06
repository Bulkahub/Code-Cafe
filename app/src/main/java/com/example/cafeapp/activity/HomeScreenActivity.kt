package com.example.cafeapp.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cafeapp.R
import com.example.cafeapp.authmanager.AuthManager
import com.google.android.material.button.MaterialButton

class HomeScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //Проверка авторизации: если пользователь уже вошел - переходим на экран меню.
        val authManager = AuthManager(this)
        if (authManager.isLoggedIn()) {
            val intent = Intent(this, MenuScreenActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        //Обработка нажатия на кнопку начать - переход к экрану логина.
        val letsGetStartedButton = findViewById<Button>(R.id.buttonGetStarted)
        letsGetStartedButton.setOnClickListener {
            val intent = Intent(this, LoginScreenActivity::class.java)
            startActivity(intent)
        }
    }
}