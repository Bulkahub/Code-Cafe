package com.example.cafeapp

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.cafeapp.databinding.ActivityLoginScreenBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class LoginScreenActivity : AppCompatActivity() {

    lateinit var textViewCreateAccount: TextView
    lateinit var binding: ActivityLoginScreenBinding

    private lateinit var auth: FirebaseAuth

    private lateinit var firestore: FirebaseFirestore

    //Всторенный интерфейс ,который позволяет хранить данные в виде ключ + значение.
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login_screen)


        textViewCreateAccount = findViewById(R.id.createAccountTextView)
        textViewCreateAccount.setOnClickListener {
            val intent = Intent(this, CreateAccountScreenActivity::class.java)
            startActivity(intent)
        }


        firestore = FirebaseFirestore.getInstance()

        auth = Firebase.auth

        sharedPreferences = getSharedPreferences("CafeAppPref", MODE_PRIVATE)
    val userRef = firestore.collection("Users")

        binding.buttonLogin.setOnClickListener {
            loginWithUserName(
                binding.loginUserName.text.toString().trim(),
                binding.loginPassword.text.toString().trim(),
                userRef
            )
        }
    }



    fun loginWithUserName(userName: String, password: String, userRef: CollectionReference) {
        userRef.whereEqualTo("userName", userName).get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val user = documents.documents[0]
                    val storedPassword = user["password"] as String

                    if (storedPassword == password) {
                        Log.d("Firestore", "Login successful! UID: ${user["uid"]}")

                        //Сохраняем данные пользователя,что бы при повторном запуске он был авторизован
                        sharedPreferences.edit().putString("loggedInUser", userName).apply()

                        //Переход на экран меню.
                        val intent = Intent(this, MenuScreenActivity::class.java)
                        intent.putExtra("username", userName)
                        startActivity(intent)
                        finish()

                    } else {
                        Log.e("Firestore", "Login Error: Incorrect password")
                        Toast.makeText(this,"Incorrect password", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("Firestore", "Login Error: User not found")
                    Toast.makeText(this,"User not found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { Log.e("Firestore", "Error Data Base") }
        Toast.makeText(this,"Error Data Base!", Toast.LENGTH_SHORT).show()
    }

}