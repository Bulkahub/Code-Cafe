package com.example.cafeapp.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.example.cafeapp.R
import com.example.cafeapp.databinding.ActivityCoffeItemActvityBinding

class CoffeItemActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCoffeItemActvityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = DataBindingUtil.setContentView(this,R.layout.activity_coffe_item_actvity)

        val coffeDescription = intent.getStringExtra("COFFE_DESCRIPTION")
        val coffeName = intent.getStringExtra("COFFE_NAME")
        val coffeImage = intent.getIntExtra("COFFE_IMAGE",-1)
        if(coffeImage != -1){
            binding.coffeItemImage.setImageResource(coffeImage)
        }
        binding.textViewCardViewCoffeItem.text = coffeName
        binding.thoughtsTextViewDescription.text = coffeDescription
    }
}