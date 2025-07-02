package com.example.cafeapp.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.cafeapp.databinding.ActivityCoffeItemActvityBinding
import com.example.cafeapp.navigationkeys.NavigationKeys
import com.example.cafeapp.view.CartViewModel

//Экран с подробным описанием выбраноного кофе и выбором его размера.
class CoffeItemActivity : AppCompatActivity() {

    // ViewModel для взаимодействия с корзиной
    private lateinit var cartViewModel: CartViewModel

    // ViewBinding для доступа к UI-элементам
    private lateinit var binding: ActivityCoffeItemActvityBinding

    // Данные о кофе, полученные из Intent
    private var coffeName: String? = null
    private var coffeImage: Int = -1
    private var coffeePrice: String = "0"//Знач. по умолчанию если не выставлено.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Инициализация ViewBinding.
        binding = ActivityCoffeItemActvityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Получаем ViewModel для работы с корзиной.
        cartViewModel = ViewModelProvider(this)[CartViewModel::class.java]

        // Извлекаем данные о кофе из Intent.
        coffeName = intent.getStringExtra(NavigationKeys.NAME)
        coffeImage = intent.getIntExtra(NavigationKeys.IMAGE, -1)
        coffeePrice = intent.getStringExtra(NavigationKeys.PRICE) ?: "0"
        val coffeDescription = intent.getStringExtra(NavigationKeys.DESCRIPTION)

        // Отображаем описание и название кофе в UI.
        binding.thoughtsTextViewDescription.text = coffeDescription
        binding.textViewCardViewCoffeItem.text = coffeName

        // Блокируем повторное нажатие, чтобы избежать дублирования в корзине.
        var buttonClicked = false

        // Обработка выбора размера кофе.
        binding.materialButtonS.setOnClickListener {
            if (!buttonClicked) {
                buttonClicked = true
                addToCartWithSize("S")
            }
        }
        binding.materialButtonM.setOnClickListener {
            if (!buttonClicked) {
                buttonClicked = true
                addToCartWithSize("M")
            }
        }
        binding.materialButtonL.setOnClickListener {
            if (!buttonClicked) {
                buttonClicked = true
                addToCartWithSize("L")
            }
        }
    }

    // Передаёт выбранный кофе и размер обратно в меню и открывает корзину.
    fun addToCartWithSize(selectedSize: String) {
        val name = coffeName ?: return
        val price = coffeePrice
        val image = coffeImage

        // Формируем Intent и передаём выбранные параметры.
        val intent = Intent(this, MenuScreenActivity::class.java).apply {
            putExtra(NavigationKeys.OPEN_CART, true)
            putExtra(NavigationKeys.NAME, name)
            putExtra(NavigationKeys.SIZE, selectedSize) // 💥 вот он — размер
            putExtra(NavigationKeys.PRICE, price)
            putExtra(NavigationKeys.IMAGE, image)
        }
        startActivity(intent)
        finish()
    }
}