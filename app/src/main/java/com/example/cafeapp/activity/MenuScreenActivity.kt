package com.example.cafeapp.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.cafeapp.R
import com.example.cafeapp.databinding.ActivityMenuScreenBinding

class MenuScreenActivity : AppCompatActivity() {

    lateinit var binding: ActivityMenuScreenBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_menu_screen)


    }
}