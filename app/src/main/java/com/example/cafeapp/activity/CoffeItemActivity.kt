package com.example.cafeapp.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.cafeapp.databinding.ActivityCoffeItemActvityBinding
import com.example.cafeapp.dataclass.CartItem
import com.example.cafeapp.dataclass.NotificationData
import com.example.cafeapp.navigationkeys.NavigationKeys
import com.example.cafeapp.view.CartViewModel
import com.example.cafeapp.view.NotificationViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.UUID
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Screen with detailed description of the selected coffee and size selection.
@AndroidEntryPoint
class CoffeItemActivity : AppCompatActivity() {
    // Variable to prevent repeated clicks on the selection button.
    private var buttonClicked = false

    // ViewModel for interacting with the cart.
    private val cartViewModel: CartViewModel by viewModels()

    // ViewModel for managing notifications.
    private val notificationViewModel: NotificationViewModel by viewModels()

    // ViewBinding for accessing UI elements.
    private lateinit var binding: ActivityCoffeItemActvityBinding

    // Coffee data received from Intent.
    private var coffeName: String? = null
    private var coffeImage: Int = -1
    private var coffeePrice: String = "0"// Default value if not set.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initializing ViewBinding.
        binding = ActivityCoffeItemActvityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Getting coffee name from Intent. There's a redundant call, but it will be overridden below.
        coffeName = intent.getStringExtra("coffee_name") ?: "No name"

        // Extracting coffee data from Intent.
        coffeName = intent.getStringExtra(NavigationKeys.NAME)
        coffeImage = intent.getIntExtra(NavigationKeys.IMAGE, -1)
        coffeePrice = intent.getStringExtra(NavigationKeys.PRICE) ?: "0"
        val coffeDescription = intent.getStringExtra(NavigationKeys.DESCRIPTION)

        // Displaying coffee description and name in the UI.
        binding.thoughtsTextViewDescription.text = coffeDescription
        binding.textViewCardViewCoffeItem.text = coffeName


        // Handling coffee size selection.
        binding.materialButtonS.setOnClickListener { handleSelection("S") }
        binding.materialButtonM.setOnClickListener { handleSelection("M") }
        binding.materialButtonL.setOnClickListener { handleSelection("L") }
    }

    // Creates a CartItem object with the selected size and provided parameters.
    private fun makeCartItem(size: String): CartItem {
        return CartItem(
            id = UUID.randomUUID().toString(),
            size = size,
            quantity = 1,
            image = coffeImage,
            price = coffeePrice,
            name = coffeName ?: "Unknown"
        )
    }

    // Handles coffee size selection: adds to cart, creates notifications, and closes the screen.
    private fun handleSelection(size: String) {
        if (buttonClicked) return
        buttonClicked = true

        val item = makeCartItem(size)
        notificationViewModel.notifyNow(
            "Your coffee size $size is being prepared!",
            NotificationData.Type.INFO
        )
        notificationViewModel.notifyLater(
            "Your coffee is ready! Pick it up from the barista.", delayMillis = 10000,
            NotificationData.Type.SUCCESS
        )

        // Adds the item to the cart and returns to the main screen.
        lifecycleScope.launch {
            cartViewModel.addToCart(item)
            delay(150)

            // Navigates to MenuScreenActivity with a flag to open the cart.
            val intent = Intent(this@CoffeItemActivity, MenuScreenActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                putExtra("open_cart", true)
            }
            startActivity(intent)
            finish()
        }
    }
}
