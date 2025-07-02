package com.example.cafeapp.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cafeapp.dataclass.CartItem
import com.example.cafeapp.databinding.CartItemBinding

// Адаптер для отображения элементов корзины в RecyclerView.
class CartAdapter(private val cartList: MutableList<CartItem>) :
    RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    // ViewHolder — связывает данные из CartItem с layout-элементами.
    inner class CartViewHolder(private val binding: CartItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // Привязываем данные товара к представлению.
        fun bind(item: CartItem) {
            binding.textViewCartName.text = item.name
            binding.textViewCartSize.text = "Size: ${item.size}"
            binding.textViewCartPrice.text = item.price
            binding.imageViewCart.setImageResource(item.image)
        }
    }

    // Создаём ViewHolder, используя layout для элемента корзины.
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CartViewHolder {
        val binding = CartItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    // Передаём данные в конкретный ViewHolder.
    override fun onBindViewHolder(
        holder: CartViewHolder,
        position: Int
    ) {
        holder.bind(cartList[position])
    }

    // Количество элементов для отображения.
    override fun getItemCount(): Int {
        return cartList.size
    }

    // Обновляем список данных в адаптере и оповещаем об изменениях.
    fun updatedData(newList: List<CartItem>) {
        cartList.clear()
        cartList.addAll(newList)
        notifyDataSetChanged()
    }

}