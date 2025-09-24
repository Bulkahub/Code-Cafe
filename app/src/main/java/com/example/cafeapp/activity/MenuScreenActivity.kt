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

// Main screen of the app with coffee menu and special offers.
@AndroidEntryPoint
class MenuScreenActivity : AppCompatActivity() {

    // ViewBinding for accessing UI elements.
    lateinit var binding: ActivityMenuScreenBinding

    // ViewModel for displaying the coffee list.
    private lateinit var viewModel: MenuViewModel

    // ViewModel for managing the cart.
    private val cartViewModel: CartViewModel by viewModels()

    // ViewModel for managing notifications.
    private val notificationViewModel: NotificationViewModel by viewModels()

    // Adapter for the main coffee list.
    private lateinit var adapter: CoffeAdapter

    // Adapter for special offers.
    private lateinit var adapterSpecialOffer: CoffeAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        // Bind layout using DataBinding.
        binding = DataBindingUtil.setContentView(this, R.layout.activity_menu_screen)

        // Initialize AuthManager and get current user ID.
        val authManager = AuthManager(this)
        val userId = authManager.getUserId()
        Toast.makeText(this, "UserId = $userId", Toast.LENGTH_SHORT).show()

        // Initialize ViewModels.
        viewModel = ViewModelProvider(this)[MenuViewModel::class.java]

        // Set up navigation using BottomNavigationView + NavController.
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as? NavHostFragment
        val navController =
            navHostFragment?.navController ?: throw IllegalStateException("NavController no find")
        binding.bottomNavigationView.setupWithNavController(navController)

        // List of items already added to the cart.
        val cartList = mutableListOf<CoffeItem>()

        // Adapter for displaying regular coffee items.
        adapter = CoffeAdapter(mutableListOf(), cartList) { selectedCoffe ->
            val intent = Intent(this, CoffeItemActivity::class.java).apply {
                putExtra(NavigationKeys.NAME, selectedCoffe.name)
                putExtra(NavigationKeys.IMAGE, selectedCoffe.imageRecId)
                putExtra(NavigationKeys.DESCRIPTION, selectedCoffe.description)
                putExtra(NavigationKeys.PRICE, selectedCoffe.price)
            }
            startActivity(intent)
        }

        // Adapter for displaying special offers (can be improved in future).
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

        // Set up RecyclerViews for regular and special items.
        binding.recyclerViewCoffeItem.layoutManager = GridLayoutManager(this, 2)
        binding.recyclerViewCoffeItem.adapter = adapter

        binding.recyclerViewSpecialOffer.layoutManager = GridLayoutManager(this, 1)
        binding.recyclerViewSpecialOffer.adapter = adapterSpecialOffer


        // Load data and observe changes.
        viewModel.loadCoffeList()
        viewModel.filteredList.observe(this) { fullList ->
            adapter.updateData(fullList.toMutableList())
            binding.recyclerViewCoffeItem.visibility = View.VISIBLE

        }
        // Observe special offers list.
        viewModel.specialOfferList.observe(this) { specialOffers ->
            adapterSpecialOffer.updateData(specialOffers.toMutableList())
            binding.recyclerViewSpecialOffer.visibility = View.VISIBLE
        }


        // Filter by coffee name.
        binding.cappuccinoTextView.setOnClickListener { viewModel.showCoffeType("Cappuccino") }
        binding.espressoTextView.setOnClickListener { viewModel.showCoffeType("Espresso") }
        binding.latteTextView.setOnClickListener { viewModel.showCoffeType("Latte") }
        binding.macchiatoTextView.setOnClickListener { viewModel.showCoffeType("Macchiato") }
        binding.americanoTextView.setOnClickListener { viewModel.showCoffeType("Americano") }
        binding.hotChocolateTextView.setOnClickListener { viewModel.showCoffeType("Hot Chocolate") }


        // Handle search by coffee name.
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

        // Navigation via bottom menu (home / cart).
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
                    navController.navigate(R.id.homeFragment)
                    true
                }

                R.id.nav_cart -> {
                    // Navigate to cart via NavController.
                    binding.navHostFragment.visibility = View.VISIBLE
                    val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
                    navController.navigate(R.id.cartFragment)
                    true
                }

                R.id.nav_favorites -> {
                    // Navigate to favorites screen.
                    binding.navHostFragment.visibility = View.VISIBLE
                    val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
                    navController.navigate(R.id.nav_favorites)
                    true
                }

                R.id.nav_notification -> {
                    // Navigate to notifications screen.
                    binding.navHostFragment.visibility = View.VISIBLE
                    val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
                    navController.navigate(R.id.nav_notification)
                    true
                }

                else -> false
            }
        }

        // Track cart item count and show badge on icon.
        lifecycleScope.launchWhenStarted {
            cartViewModel.cartSize.collect { size ->
                val badge = binding.bottomNavigationView.getOrCreateBadge(R.id.nav_cart)
                badge.isVisible = size > 0
                badge.number = size
            }
        }

        // Track favorite count and update badge.
        lifecycleScope.launchWhenStarted {
            cartViewModel.favoriteCount.collect { countFav ->
                val badgeFav = binding.bottomNavigationView.getOrCreateBadge(R.id.nav_favorites)
                badgeFav.isVisible = countFav > 0
                badgeFav.number = countFav
            }
        }

        // Track unread notifications count.
        lifecycleScope.launchWhenStarted {
            notificationViewModel.unreadCount.collect { countNotif ->
                val badgeNotif =
                    binding.bottomNavigationView.getOrCreateBadge(R.id.nav_notification)
                badgeNotif.isVisible = countNotif > 0
                badgeNotif.number = countNotif
                badgeNotif.maxCharacterCount = 4
            }
        }

        // Logout button handler. Describes actions when logging out.
        binding.logoutIcon.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Exit")
                .setMessage("Are you sure you want to log out of your account?")
                .setPositiveButton("Yes") { _, _ -> logout() }
                .setNegativeButton("No", null)
                .show()
        }
    }

    // onResume method left empty — can be used for updates when returning to the screen.
    override fun onResume() {
        super.onResume()
    }

    // Logout navigation.
    private fun logout() {
        val authManager = AuthManager(this)
        authManager.clearSession()

        val intent = Intent(this, HomeScreenActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}

