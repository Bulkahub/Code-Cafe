package com.example.cafeapp.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cafeapp.R
import com.example.cafeapp.adapter.CartAdapter
import com.example.cafeapp.adapter.DisplayMode
import com.example.cafeapp.dataclass.CartItem
import com.example.cafeapp.view.CartViewModel

/** Фрагмент, отображающий содержимое корзины пользователя.*/
class CartFragment : Fragment() {

    // Получаем доступ к общей ViewModel через activityViewModels (поддержка shared state между фрагментами).
    private val cartViewModel by activityViewModels<CartViewModel>()
    private lateinit var recyclerView: RecyclerView //Список оваров в корзине.
    private lateinit var cartAdapter: CartAdapter   // Адаптер для отображения элементов корзины.

    // Внутренний список, может использоваться при необходимости локального кеша.
    private val cartList = mutableListOf<CartItem>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Инициализация RecyclerView и установка менеджера с 2 колонками.
        recyclerView = view.findViewById(R.id.recyclerViewCart)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.visibility = View.GONE

        val progressBar = view.findViewById<ProgressBar>(R.id.progressBarCart)
        recyclerView.visibility = View.GONE
        progressBar.visibility = View.VISIBLE

        // Инициализация адаптера для отображения товаров в корзине.
        //   → товар добавляется в список избранных через cartViewModel.addToFavorites()
        cartAdapter = CartAdapter(
            onFavoriteClick = { item ->
                cartViewModel.addToFavorites(item)
                Toast.makeText(context, "Add To Favorites", Toast.LENGTH_SHORT).show()
            },

            onRemoveClick = { itemToRemove ->
                cartViewModel.removeCart(itemToRemove)
                Toast.makeText(context, "Delete From Cart", Toast.LENGTH_SHORT).show()
            },
            mode = DisplayMode.CART
        )

        recyclerView.adapter = cartAdapter


        // Отслеживание изменений списка корзины.
        lifecycleScope.launchWhenStarted {
            cartViewModel.cartItems.collect { cartItems ->
                cartAdapter.submitList(cartItems) // Обновляем адаптер с новым списком товаров (DiffUtil).

                // Управление видимостью: если список пуст — показываем прогрессбар, иначе — список.
                recyclerView.visibility = if (cartItems.isEmpty()) View.GONE else View.VISIBLE
                progressBar.visibility = if (cartItems.isEmpty()) View.VISIBLE else View.GONE

            }
        }
    }
}





