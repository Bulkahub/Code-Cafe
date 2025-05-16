package com.example.cafeapp

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.example.cafeapp.databinding.ActivityMenuScreenBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MenuScreenActivity : AppCompatActivity() {

    lateinit var binding: ActivityMenuScreenBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = DataBindingUtil.setContentView(this,R.layout.activity_menu_screen)


    }
}