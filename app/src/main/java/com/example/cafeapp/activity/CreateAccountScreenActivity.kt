package com.example.cafeapp.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.cafeapp.R
import com.example.cafeapp.databinding.ActivityCreateAccountScreenBinding
import com.example.cafeapp.dataclass.User
import com.google.firebase.firestore.FirebaseFirestore
import java.util.UUID

class CreateAccountScreenActivity : AppCompatActivity() {

    //Привязка к макету.
    lateinit var binding: ActivityCreateAccountScreenBinding

    //Firebase Firestore для хранения данных пользователей.
    private lateinit var firestore: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        //Инициализация привязки к макету.
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_account_screen)

        //Инициализация Firestore.
        firestore = FirebaseFirestore.getInstance()

        //Обработчик кнопки "Создать аккаунт".
        binding.buttonCreateAccount.setOnClickListener {
            val userName = binding.createName.text.toString()
            val password = binding.createPassword.text.toString()

            //Проверяем,что все поля не пустые.
            if (userName.isNotEmpty() && password.isNotEmpty()) {
                registerUser(userName, password)//Регистрируем пользователя.
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

    }

    //Функция регистрации пользователя.
    fun registerUser(userName: String, password: String) {
        val userId = UUID.randomUUID().toString()//Генерируем уникальный "userId"

        //Создаем объект пользователя.
        val user = User(
            uid = userId,
            userName = userName.trim(),
            password = password.trim(),
            createdAt = System.currentTimeMillis()
        )

        //Сохраняем пользователя в Firestore.
        firestore.collection("Users").document(userId)
            .set(user)
            .addOnSuccessListener {
                Log.d("Firestore", "User registered")

                // После успешного сохранения пользователя → Переход в `LoginScreenActivity`
                startActivity(Intent(this, LoginScreenActivity::class.java))
                finish()
            }
            .addOnFailureListener { Log.e("Firestore", "Error save data") }
    }
}