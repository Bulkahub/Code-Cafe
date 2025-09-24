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

/** Fragment displaying the contents of the user's cart.*/
class CartFragment : Fragment() {

    // Access shared ViewModel via activityViewModels (supports shared state between fragments).
    private val cartViewModel by activityViewModels<CartViewModel>()
    private lateinit var recyclerView: RecyclerView // List of items in the cart.
    private lateinit var cartAdapter: CartAdapter   // Adapter for displaying cart items.

    // Internal list, can be used for local caching if needed.
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

        // Initialize RecyclerView and set layout manager with 2 columns.
        recyclerView = view.findViewById(R.id.recyclerViewCart)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.visibility = View.GONE

        val progressBar = view.findViewById<ProgressBar>(R.id.progressBarCart)
        recyclerView.visibility = View.GONE
        progressBar.visibility = View.VISIBLE

        // Initialize adapter for displaying cart items.
        //   → item is added to favorites via cartViewModel.addToFavorites()
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


        // Observe changes in the cart item list.
        lifecycleScope.launchWhenStarted {
            cartViewModel.cartItems.collect { cartItems ->
                cartAdapter.submitList(cartItems) // Update adapter with new item list (DiffUtil).

                // Manage visibility: show progress bar if list is empty, otherwise show list.
                recyclerView.visibility = if (cartItems.isEmpty()) View.GONE else View.VISIBLE
                progressBar.visibility = if (cartItems.isEmpty()) View.VISIBLE else View.GONE

            }
        }
    }
}
