package com.example.cafeapp.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cafeapp.R
import com.example.cafeapp.dataclass.CartItem
import com.example.cafeapp.databinding.CartItemBinding

// Enumeration to define display mode: cart or favorites.
enum class DisplayMode {
    CART,
    FAVORITE
}

// Adapter for displaying cart or favorite items in RecyclerView.
class CartAdapter(
    private val onFavoriteClick: ((CartItem) -> Unit)? = null, // Callback when the "favorite" icon is clicked.
    private val onRemoveClick: ((CartItem) -> Unit)? = null, // Callback for removing an item.
    private val mode: DisplayMode = DisplayMode.CART // Default display mode — cart.
) :
    ListAdapter<CartItem, CartAdapter.CartViewHolder>(CartDiffCallback) { // Using DiffUtil for efficient updates.

    // ViewHolder — binds CartItem data to layout elements.
    inner class CartViewHolder(private val binding: CartItemBinding) :
        RecyclerView.ViewHolder(binding.root) {


        // Method to bind CartItem data to UI elements.
        fun bind(item: CartItem) {
            binding.textViewCartName.text = item.name
            binding.textViewCartSize.text = "Size: ${item.size}"
            binding.textViewCartPrice.text = item.price
            binding.imageViewCart.setImageResource(item.image)

            // Handle clicks depending on the mode: cart or favorites.
            when (mode) {
                DisplayMode.CART -> {
                    binding.heartFavorite.setImageResource(R.drawable.heartcartitem)
                    binding.heartFavorite.setOnClickListener {
                        onFavoriteClick?.invoke(item)
                    }
                    // Clear button — removes item from cart.
                    binding.clearCoffe.setOnClickListener {
                        onRemoveClick?.invoke(item)
                    }
                }

                DisplayMode.FAVORITE -> {
                    binding.heartFavorite.setImageResource(R.drawable.delete_coffe)
                    binding.heartFavorite.visibility = View.VISIBLE
                    binding.heartFavorite.setOnClickListener {
                        onRemoveClick?.invoke(item)
                    }
                    // Hide the remove-from-cart button.
                    binding.clearCoffe.visibility = View.GONE
                    binding.clearCoffe.setOnClickListener(null)
                }
            }
        }
    }

    // Object for comparing list items — optimized via DiffUtil.
    object CartDiffCallback : DiffUtil.ItemCallback<CartItem>() {
        override fun areItemsTheSame(
            oldItem: CartItem,
            newItem: CartItem
        ): Boolean {
            return oldItem.id == newItem.id
        }

        // Compare contents — if identical, no need to redraw.
        override fun areContentsTheSame(
            oldItem: CartItem,
            newItem: CartItem
        ): Boolean {
            return oldItem.name == newItem.name &&
                    oldItem.size == newItem.size &&
                    oldItem.price == newItem.price &&
                    oldItem.quantity == newItem.quantity &&
                    oldItem.image == newItem.image &&
                    oldItem.id == newItem.id
        }

    }

    // Create ViewHolder using the layout for cart item.
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CartViewHolder {
        val binding = CartItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    // Pass data to the specific ViewHolder.
    override fun onBindViewHolder(
        holder: CartViewHolder,
        position: Int
    ) {
        val item = getItem(position)
        holder.bind(item)
    }
}