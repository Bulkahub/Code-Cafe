package com.example.cafeapp.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.cafeapp.R
import com.example.cafeapp.adapter.CoffeAdapter
import com.example.cafeapp.databinding.ActivityMenuScreenBinding
import com.example.cafeapp.dataclass.CoffeItem
import com.example.cafeapp.view.MenuViewModel

class MenuScreenActivity : AppCompatActivity() {

    lateinit var binding: ActivityMenuScreenBinding

    private lateinit var viewModel: MenuViewModel
    private lateinit var adapter: CoffeAdapter
    private lateinit var adapterSpecialOffer: CoffeAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_menu_screen)


        val cartList = mutableListOf<CoffeItem>()

        viewModel = ViewModelProvider(this)[MenuViewModel::class.java]
        adapter = CoffeAdapter(mutableListOf(), cartList) { selectedCoffe ->
            addToCart(selectedCoffe)
        }

        adapterSpecialOffer = CoffeAdapter(mutableListOf(), cartList) { selectedCoffe ->
            addToCart(selectedCoffe)
        }

        binding.recyclerViewCoffeItem.layoutManager = GridLayoutManager(this, 2)
        binding.recyclerViewCoffeItem.adapter = adapter

        binding.recyclerViewSpecialOffer.layoutManager = GridLayoutManager(this, 1)
        binding.recyclerViewSpecialOffer.adapter = adapterSpecialOffer


        //Загружаем данные.
        viewModel.loadCoffeList()
        viewModel.filteredList.observe(this) { fullList ->
            if(fullList.size == 2){
            Log.d("DEBUG", "RecyclerView получает два элемента: ${fullList.map { it.imageRecId }}")
            adapter.updateData(fullList.toMutableList())
            }else{
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

        }
        fun addToCart(coffe: CoffeItem) {
            Toast.makeText(this, "${coffe.name} Added To Cart!", Toast.LENGTH_SHORT).show()
    }
}
