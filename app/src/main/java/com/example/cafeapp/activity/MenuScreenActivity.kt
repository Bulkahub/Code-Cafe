package com.example.cafeapp.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.cafeapp.R
import com.example.cafeapp.adapter.CoffeAdapter
import com.example.cafeapp.databinding.ActivityMenuScreenBinding
import com.example.cafeapp.dataclass.CartItem
import com.example.cafeapp.dataclass.CoffeItem
import com.example.cafeapp.navigationkeys.NavigationKeys
import com.example.cafeapp.view.CartViewModel
import com.example.cafeapp.view.MenuViewModel
import kotlin.jvm.java

// Главный экран приложения с меню кофе и спецпредложениями.
class MenuScreenActivity : AppCompatActivity() {

    // ViewBinding для доступа к UI-элементам.
    lateinit var binding: ActivityMenuScreenBinding

    // ViewModel для отображения списка кофе.
    private lateinit var viewModel: MenuViewModel

    // ViewModel для управления корзиной.
    private lateinit var cartViewModel: CartViewModel

    // Адаптер для основного списка кофе.
    private lateinit var adapter: CoffeAdapter

    // Адаптер для спецпредложений.
    private lateinit var adapterSpecialOffer: CoffeAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Привязываем layout через DataBinding.
        binding = DataBindingUtil.setContentView(this, R.layout.activity_menu_screen)

        // Инициализируем ViewModels.
        cartViewModel = ViewModelProvider(this)[CartViewModel::class.java]
        viewModel = ViewModelProvider(this)[MenuViewModel::class.java]

        // Настраиваем навигацию с помощью BottomNavigationView + NavController.
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as? NavHostFragment
        val navController =
            navHostFragment?.navController ?: throw IllegalStateException("NavController no find")
        binding.bottomNavigationView.setupWithNavController(navController)

        // Список товаров, уже добавленных в корзину.
        val cartList = mutableListOf<CoffeItem>()

        // Адаптер для отображения обычных позиций.
        adapter = CoffeAdapter(mutableListOf(), cartList) { selectedCoffe ->
            val intent = Intent(this, CoffeItemActivity::class.java).apply {
                putExtra(NavigationKeys.NAME, selectedCoffe.name)
                putExtra(NavigationKeys.IMAGE, selectedCoffe.imageRecId)
                putExtra(NavigationKeys.DESCRIPTION, selectedCoffe.description)
                putExtra(NavigationKeys.PRICE, selectedCoffe.price)
            }
            startActivity(intent)
        }

        // Адаптер для отображения спецпредложений(в будущем можно доработать).
        adapterSpecialOffer = CoffeAdapter(mutableListOf(), cartList) { selectedCoffe ->
            val intent = Intent(this, CoffeItemActivity::class.java).apply {
                putExtra(NavigationKeys.NAME, selectedCoffe.name)
                putExtra(NavigationKeys.IMAGE, selectedCoffe.imageRecId)
                putExtra(NavigationKeys.DESCRIPTION, selectedCoffe.description)
                putExtra(NavigationKeys.PRICE, selectedCoffe.price)
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            }
            startActivity(intent)
        }

        // Настраиваем RecyclerView для обычных и спецпозиций.
        binding.recyclerViewCoffeItem.layoutManager = GridLayoutManager(this, 2)
        binding.recyclerViewCoffeItem.adapter = adapter

        binding.recyclerViewSpecialOffer.layoutManager = GridLayoutManager(this, 1)
        binding.recyclerViewSpecialOffer.adapter = adapterSpecialOffer


        // Загружаем данные и подписываемся на изменения.
        viewModel.loadCoffeList()
        viewModel.filteredList.observe(this) { fullList ->
            if (fullList.size == 2) {
                // Обновляем основной список.
                adapter.updateData(fullList.toMutableList())
            } else {
                // Не отображаем, если список неполный.
                Log.e("DEBUG", "Ошибка! В списке только один элемент. Не обновляем `RecyclerView`.")
            }
            binding.recyclerViewCoffeItem.visibility = View.VISIBLE

        }

        viewModel.specialOfferList.observe(this) { specialOffers ->
            adapterSpecialOffer.updateData(specialOffers.toMutableList())
            binding.recyclerViewSpecialOffer.visibility = View.VISIBLE
        }


        //Фильтрация по названию кофе.
        binding.cappuccinoTextView.setOnClickListener { viewModel.showCoffeType("Cappuccino") }
        binding.espressoTextView.setOnClickListener { viewModel.showCoffeType("Espresso") }
        binding.latteTextView.setOnClickListener { viewModel.showCoffeType("Latte") }
        binding.macchiatoTextView.setOnClickListener { viewModel.showCoffeType("Macchiato") }
        binding.americanoTextView.setOnClickListener { viewModel.showCoffeType("Americano") }
        binding.hotChocolateTextView.setOnClickListener { viewModel.showCoffeType("Hot Chocolate") }


        // Обработка поиска по названию.
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { viewModel.filterCoffe(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { viewModel.filterCoffe(it) }
                return true
            }
        })

        // Навигация по нижнему меню (домой / корзина).
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    val intent = Intent(this, MenuScreenActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.nav_cart -> {
                    // Переход в корзину через NavController.
                    binding.navHostFragment.visibility = View.VISIBLE
                    val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
                    navController.navigate(R.id.cartFragment)
                    true
                }

                else -> false
            }
        }
    }

    // Проверяем, переданы ли данные о товаре — и добавляем его в корзину.
    override fun onResume() {
        super.onResume()

        if (intent.getBooleanExtra(NavigationKeys.OPEN_CART, false)) {
            val name = intent.getStringExtra(NavigationKeys.NAME) ?: return
            val size = intent.getStringExtra(NavigationKeys.SIZE) ?: "Unknown"
            val price = intent.getStringExtra(NavigationKeys.PRICE) ?: "Price not set"
            val image = intent.getIntExtra(NavigationKeys.IMAGE, -1)

            val item = CartItem(name, size, price, image)
            cartViewModel.addToCart(item)

            // Переход в корзину.
            findNavController(R.id.nav_host_fragment).navigate(R.id.cartFragment)

            // Убираем флаг, чтобы не повторно не триггерить добавление.
            intent.removeExtra(NavigationKeys.OPEN_CART)
        }
    }

}

