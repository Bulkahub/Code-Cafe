package com.example.cafeapp.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cafeapp.activity.CoffeItemActivity
import com.example.cafeapp.databinding.ItemCoffeBinding
import com.example.cafeapp.databinding.ItemSpecialOfferBinding
import com.example.cafeapp.dataclass.CoffeItem
import com.example.cafeapp.navigationkeys.NavigationKeys

// Адаптер для отображения списка кофе и спецпредложений.
class CoffeAdapter(
    private val coffeList: MutableList<CoffeItem> = mutableListOf(),
    private val cartList: MutableList<CoffeItem>,// Список товаров, уже добавленных в корзину.
    private val onAddClick: (CoffeItem) -> Unit // Callback при нажатии на иконку "добавить".
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_COFFEE = 0   // Тип обычной позиции.
        private const val VIEW_TYPE_SPECIAL = 1   // Тип спецпредложения.
    }

    // Отфильтрованный список, отображаемый в RecyclerView.
    private var filteredList = coffeList.toMutableList()

    // Возвращаем тип элемента — используется для выбора ViewHolder.
    override fun getItemViewType(position: Int): Int {
        return if (filteredList[position].name == "Special Offer") VIEW_TYPE_SPECIAL else VIEW_TYPE_COFFEE
    }

    // Создаём соответствующий ViewHolder в зависимости от типа элемента.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_SPECIAL) {
            val binding =
                ItemSpecialOfferBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            SpecialOfferViewHolder(binding)
        } else {
            val binding =
                ItemCoffeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            CoffeViewHolder(binding)
        }
    }

    // Привязываем данные к ViewHolder в зависимости от типа.
    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        if (holder is SpecialOfferViewHolder) {
            holder.bind(filteredList[position])
        } else if (holder is CoffeViewHolder) {
            holder.bind(filteredList[position])
        }
    }

    // Количество отображаемых элементов.
    override fun getItemCount() = filteredList.size

    // ViewHolder для обычных позиций кофе.
    inner class CoffeViewHolder(private val binding: ItemCoffeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CoffeItem) {
            // Привязываем данные к UI.
            binding.textViewCofeeName.text = item.name
            binding.textViewCofFeMilk.text = item.milkType
            binding.textViewPrice.text = item.price


            binding.imageViewCoffe.setImageResource(item.imageRecId)

            // Меняем прозрачность кнопки добавления, если уже в корзине.
            if (cartList.contains(item)) {
                binding.imageViewAdd.animate().alpha(0.5f).setDuration(300)
            } else {
                binding.imageViewAdd.animate().alpha(1.0f).setDuration(300)
            }

            // Обработка клика по иконке добавления.
            binding.imageViewAdd.setOnClickListener {
                onAddClick(item)
                notifyItemChanged(adapterPosition) // Обновляем текущую позицию.

                // Переход к подробной информации о кофе.
                val intent = Intent(itemView.context, CoffeItemActivity::class.java).apply {
                    putExtra(NavigationKeys.NAME, item.name)
                    putExtra(NavigationKeys.IMAGE, item.imageRecId)
                    putExtra(NavigationKeys.DESCRIPTION, item.description)
                    putExtra(NavigationKeys.PRICE, item.price)

                }
                itemView.context.startActivity(intent)
            }
        }
    }

    // ViewHolder для блока спецпредложения.
    inner class SpecialOfferViewHolder(private val binding: ItemSpecialOfferBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CoffeItem) {
            binding.SpecialOfferTextView.text = item.milkType
            binding.imageSpecialOffer.setImageResource(item.imageRecId)
        }
    }

    // Метод для фильтрации списка кофе.
    fun filter(query: String) {
        filteredList =
            coffeList.filter {
                it.name.contains(
                    query,
                    ignoreCase = true
                ) || it.name == "Special Offer"
            }.toMutableList()
        notifyDataSetChanged()
    }

    // Обновляем отображаемый список кофе и уведомляем адаптер об изменениях.
    fun updateData(newList: List<CoffeItem>) {
        if (newList.isEmpty()) return // Пропускаем пустые обновления.

        // Очищаем предыдущие данные и добавляем новые.
        filteredList.clear()
        filteredList.addAll(newList)

        // Уведомляем адаптер о полном обновлении данных.
        notifyDataSetChanged()
    }
}