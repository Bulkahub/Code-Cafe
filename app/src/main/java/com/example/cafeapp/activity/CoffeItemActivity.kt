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

//Экран с подробным описанием выбраноного кофе и выбором его размера.
@AndroidEntryPoint
class CoffeItemActivity : AppCompatActivity() {
    // Переменная для блокировки повторных нажатий на кнопку выбора.
    private var buttonClicked = false

    // ViewModel для взаимодействия с корзиной
    private val cartViewModel: CartViewModel by viewModels()

    // ViewModel для управления уведомлениями.
    private val notificationViewModel: NotificationViewModel by viewModels()

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

        // Получаем название кофе из Intent. Есть лишний вызов, но он будет переопределён ниже.
        coffeName = intent.getStringExtra("coffee_name") ?: "No name"

        // Извлекаем данные о кофе из Intent.
        coffeName = intent.getStringExtra(NavigationKeys.NAME)
        coffeImage = intent.getIntExtra(NavigationKeys.IMAGE, -1)
        coffeePrice = intent.getStringExtra(NavigationKeys.PRICE) ?: "0"
        val coffeDescription = intent.getStringExtra(NavigationKeys.DESCRIPTION)

        // Отображаем описание и название кофе в UI.
        binding.thoughtsTextViewDescription.text = coffeDescription
        binding.textViewCardViewCoffeItem.text = coffeName


        // Обработка выбора размера кофе.
        binding.materialButtonS.setOnClickListener { handleSelection("S") }
        binding.materialButtonM.setOnClickListener { handleSelection("M") }
        binding.materialButtonL.setOnClickListener { handleSelection("L") }
    }

    // Создаёт объект CartItem с выбранным размером и переданными параметрами.
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

    // Обрабатывает выбор размера кофе: добавляет в корзину, создаёт уведомление и закрывает экран.
    private fun handleSelection(size: String) {
        if (buttonClicked) return
        buttonClicked = true

        val item = makeCartItem(size)
        notificationViewModel.notifyNow(
            "Ваш кофе размера $size готовится!",
            NotificationData.Type.INFO
        )
        notificationViewModel.notifyLater(
            "Ваш кофе готов! Заберите у бариста.", delayMillis = 10000,
            NotificationData.Type.SUCCESS
        )

        // Добавляем товар в корзину и переходим обратно на главный экран.
        lifecycleScope.launch {
            cartViewModel.addToCart(item)
            delay(150)

            // Переход на MenuScreenActivity с флагом открытия корзины.
            val intent = Intent(this@CoffeItemActivity, MenuScreenActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                putExtra("open_cart", true)
            }
            startActivity(intent)
            finish()
        }
    }
}
