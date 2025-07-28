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

// Перечисление для определения режима отображения: корзина или избранное.
enum class DisplayMode {
    CART,
    FAVORITE
}

// Адаптер для отображения элементов корзины или избранного в RecyclerView.
class CartAdapter(
    private val onFavoriteClick: ((CartItem) -> Unit)? = null, // Коллбэк при нажатии на иконку "избранное".
    private val onRemoveClick: ((CartItem) -> Unit)? = null, // Коллбэк для удаления товара.
    private val mode: DisplayMode = DisplayMode.CART // Режим отображения по умолчанию — корзина.
) :
    ListAdapter<CartItem, CartAdapter.CartViewHolder>(CartDiffCallback) { // Используем DiffUtil для оптимизации обновлений.

    // ViewHolder — связывает данные из CartItem с layout-элементами.
    inner class CartViewHolder(private val binding: CartItemBinding) :
        RecyclerView.ViewHolder(binding.root) {


        // Метод привязки данных CartItem к элементам интерфейса.
        fun bind(item: CartItem) {
            binding.textViewCartName.text = item.name
            binding.textViewCartSize.text = "Size: ${item.size}"
            binding.textViewCartPrice.text = item.price
            binding.imageViewCart.setImageResource(item.image)

            // Обработка кликов в зависимости от режима: корзина или избранное.
            when (mode) {
                DisplayMode.CART -> {
                    binding.heartFavorite.setImageResource(R.drawable.heartcartitem)
                    binding.heartFavorite.setOnClickListener {
                        onFavoriteClick?.invoke(item)
                    }
                    // Кнопка очистки — удаление из корзины.
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
                    // Кнопка удаления из корзины скрыта.
                    binding.clearCoffe.visibility = View.GONE
                    binding.clearCoffe.setOnClickListener(null)
                }
            }
        }
    }

    // Объект для сравнения элементов списка — оптимизация через DiffUtil.
    object CartDiffCallback : DiffUtil.ItemCallback<CartItem>() {
        override fun areItemsTheSame(
            oldItem: CartItem,
            newItem: CartItem
        ): Boolean {
            return oldItem.id == newItem.id
        }

        // Сравниваем содержимое — если всё одинаково, перерисовка не требуется.
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
        val item = getItem(position)
        holder.bind(item)
    }
}