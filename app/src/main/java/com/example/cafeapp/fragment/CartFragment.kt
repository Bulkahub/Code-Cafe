package com.example.cafeapp.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cafeapp.R
import com.example.cafeapp.adapter.CartAdapter
import com.example.cafeapp.dataclass.CartItem
import com.example.cafeapp.view.CartViewModel

// Фрагмент, отображающий содержимое корзины пользователя.
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

        // Инициализация адаптера с пустым списком.
        cartAdapter = CartAdapter(mutableListOf())
        recyclerView.adapter = cartAdapter

        // Отслеживание изменений списка корзины.
        cartViewModel.cartList.observe(viewLifecycleOwner) { cartItems ->
            // Обновление данных в адаптере и уведомление об изменениях.
            cartAdapter.updatedData(cartItems.toMutableList())
            recyclerView.post { cartAdapter.notifyDataSetChanged() }

            // Управление видимостью: если список пуст — показываем прогрессбар, иначе — список.
            recyclerView.visibility = if (cartItems.isEmpty()) View.GONE else View.VISIBLE
            progressBar.visibility = if (cartItems.isEmpty()) View.VISIBLE else View.GONE

        }
    }
}





