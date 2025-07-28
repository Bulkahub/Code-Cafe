package com.example.cafeapp.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cafeapp.R
import com.example.cafeapp.adapter.CartAdapter
import com.example.cafeapp.adapter.DisplayMode
import com.example.cafeapp.view.CartViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/** Фрагмент отображает список избранных товаров пользователя.*/
@AndroidEntryPoint
class FavoriteFragment : Fragment() {

    // Получаем общую ViewModel для взаимодействия между фрагментами.
    private val cartViewModel: CartViewModel by activityViewModels()

    // RecyclerView для отображения избранных элементов.
    private lateinit var recyclerViewFav: RecyclerView

    // Адаптер с режимом FAVORITE и обработчиком удаления.
    private lateinit var favoriteAdapter: CartAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite_cofffe, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Инициализация RecyclerView и установка менеджера с 2 колонками.
        recyclerViewFav = view.findViewById(R.id.recyclerViewFavorite)
        recyclerViewFav.layoutManager = GridLayoutManager(requireContext(), 2)

        // Настраиваем адаптер: при нажатии удаляем товар из избранного.
        favoriteAdapter = CartAdapter(
            onRemoveClick = { item ->
                cartViewModel.removeFromFavorites(item)
            },
            mode = DisplayMode.FAVORITE // Устанавливаем режим отображения
        )
        recyclerViewFav.adapter = favoriteAdapter

        // Отслеживаем изменения списка избранного.
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                cartViewModel.favoriteList.collect { favItems ->
                    favoriteAdapter.submitList(favItems.toMutableList())
                }
            }
        }
    }
}