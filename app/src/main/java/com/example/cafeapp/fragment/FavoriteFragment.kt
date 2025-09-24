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

/** Fragment displaying the user's list of favorite items.*/
@AndroidEntryPoint
class FavoriteFragment : Fragment() {

    // Access shared ViewModel for interaction between fragments.
    private val cartViewModel: CartViewModel by activityViewModels()

    // RecyclerView for displaying favorite items.
    private lateinit var recyclerViewFav: RecyclerView

    // Adapter with FAVORITE mode and remove handler.
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

        // Initialize RecyclerView and set layout manager with 2 columns.
        recyclerViewFav = view.findViewById(R.id.recyclerViewFavorite)
        recyclerViewFav.layoutManager = GridLayoutManager(requireContext(), 2)

        // Set up adapter: on click, remove item from favorites.
        favoriteAdapter = CartAdapter(
            onRemoveClick = { item ->
                cartViewModel.removeFromFavorites(item)
            },
            mode = DisplayMode.FAVORITE // Set display mode
        )
        recyclerViewFav.adapter = favoriteAdapter

        // Observe changes in the favorites list.
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                cartViewModel.favoriteList.collect { favItems ->
                    favoriteAdapter.submitList(favItems.toMutableList())
                }
            }
        }
    }
}
