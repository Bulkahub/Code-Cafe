package com.example.cafeapp.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.cafeapp.R
import com.example.cafeapp.adapter.CoffeAdapter
import com.example.cafeapp.authmanager.AuthManager
import com.example.cafeapp.databinding.ActivityMenuScreenBinding
import com.example.cafeapp.dataclass.CoffeItem
import com.example.cafeapp.navigationkeys.NavigationKeys
import com.example.cafeapp.view.CartViewModel
import com.example.cafeapp.view.MenuViewModel
import com.example.cafeapp.view.NotificationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.jvm.java

// Главный экран приложения с меню кофе и спецпредложениями.
@AndroidEntryPoint
class MenuScreenActivity : AppCompatActivity() {

    // ViewBinding для доступа к UI-элементам.
    lateinit var binding: ActivityMenuScreenBinding

    // ViewModel для отображения списка кофе.
    private lateinit var viewModel: MenuViewModel

    // ViewModel для управления корзиной.
    private val cartViewModel: CartViewModel by viewModels()

    // ViewModel для управления уведомлениями.
    private val notificationViewModel: NotificationViewModel by viewModels()

    // Адаптер для основного списка кофе.
    private lateinit var adapter: CoffeAdapter

    // Адаптер для спецпредложений.
    private lateinit var adapterSpecialOffer: CoffeAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        // Привязываем layout через DataBinding.
        binding = DataBindingUtil.setContentView(this, R.layout.activity_menu_screen)

        //Инициализация AuthManager и получение  id текущего пользователя.
        val authManager = AuthManager(this)
        val userId = authManager.getUserId()
        Toast.makeText(this, "UserId = $userId", Toast.LENGTH_SHORT).show()

        // Инициализируем ViewModels.
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
            adapter.updateData(fullList.toMutableList())
            binding.recyclerViewCoffeItem.visibility = View.VISIBLE

        }
        // Наблюдаем за списком спецпредложений.
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


        // Обработка поиска по названию кофе.
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
                    val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
                    navController.navigate(R.id.homeFragment)
                    true
                }

                R.id.nav_cart -> {
                    // Переход в корзину через NavController.
                    binding.navHostFragment.visibility = View.VISIBLE
                    val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
                    navController.navigate(R.id.cartFragment)
                    true
                }

                R.id.nav_favorites -> {
                    // Переход к экрану избранного.
                    binding.navHostFragment.visibility = View.VISIBLE
                    val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
                    navController.navigate(R.id.nav_favorites)
                    true
                }

                R.id.nav_notification -> {
                    // Переход к экрану уведомлений.
                    binding.navHostFragment.visibility = View.VISIBLE
                    val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
                    navController.navigate(R.id.nav_notification)
                    true
                }

                else -> false
            }
        }

        // Отслеживаем количество товаров в корзине и отображаем бейдж на иконке.
        lifecycleScope.launchWhenStarted {
            cartViewModel.cartSize.collect { size ->
                val badge = binding.bottomNavigationView.getOrCreateBadge(R.id.nav_cart)
                badge.isVisible = size > 0
                badge.number = size
            }
        }

        // Отслеживаем количество избранных и обновляем бейдж.
        lifecycleScope.launchWhenStarted {
            cartViewModel.favoriteCount.collect { countFav ->
                val badgeFav = binding.bottomNavigationView.getOrCreateBadge(R.id.nav_favorites)
                badgeFav.isVisible = countFav > 0
                badgeFav.number = countFav
            }
        }

        // Отслеживаем количество непрочитанных уведомлений.
        lifecycleScope.launchWhenStarted {
            notificationViewModel.unreadCount.collect { countNotif ->
                val badgeNotif =
                    binding.bottomNavigationView.getOrCreateBadge(R.id.nav_notification)
                badgeNotif.isVisible = countNotif > 0
                badgeNotif.number = countNotif
                badgeNotif.maxCharacterCount = 4
            }
        }

        //Обработчик нажатия на конопку.Описание действий при выходе из аккаунта.
        binding.logoutIcon.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Exit")
                .setMessage("Are you sure you want to log out of your account?")
                .setPositiveButton("Yes") { _, _ -> logout() }
                .setNegativeButton("No", null)
                .show()
        }
    }

    // Метод onResume оставлен пустым — можно использовать для обновлений при возврате к экрану.
    override fun onResume() {
        super.onResume()
    }

    //Навигация выхода из аккаунта.
    private fun logout() {
        val authManager = AuthManager(this)
        authManager.clearSession()

        val intent = Intent(this, HomeScreenActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}

