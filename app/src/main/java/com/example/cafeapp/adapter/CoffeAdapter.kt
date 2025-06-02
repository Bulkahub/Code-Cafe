package com.example.cafeapp.adapter

import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.graphics.drawable.RoundedBitmapDrawable
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.recyclerview.widget.RecyclerView
import com.example.cafeapp.R
import com.example.cafeapp.activity.CoffeItemActivity
import com.example.cafeapp.databinding.ItemCoffeBinding
import com.example.cafeapp.databinding.ItemSpecialOfferBinding
import com.example.cafeapp.dataclass.CoffeItem

class CoffeAdapter(
    private val coffeList: MutableList<CoffeItem> = mutableListOf(),
    private val cartList: MutableList<CoffeItem>,
    private val onAddClick: (CoffeItem) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_COFFEE = 0
        private const val VIEW_TYPE_SPECIAL = 1
    }

    private var filteredList = coffeList.toMutableList()


    //Special Offer.

    override fun getItemViewType(position: Int): Int {
        return if (filteredList[position].name == "Special Offer") VIEW_TYPE_SPECIAL else VIEW_TYPE_COFFEE
    }

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


    override fun getItemCount() = filteredList.size


    inner class CoffeViewHolder(private val binding: ItemCoffeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CoffeItem) {

            binding.textViewCofeeName.text = item.name
            binding.textViewCofFeMilk.text = item.milkType
            binding.textViewPrice.text = item.price


            binding.imageViewCoffe.setImageResource(item.imageRecId)


            if (cartList.contains(item)) {
                binding.imageViewAdd.animate().alpha(0.5f).setDuration(300)
            } else {
                binding.imageViewAdd.animate().alpha(1.0f).setDuration(300)
            }

            binding.imageViewAdd.setOnClickListener {
                val intent = Intent(itemView.context, CoffeItemActivity::class.java)

                itemView.context.startActivity(intent)
                onAddClick(item)
                notifyItemChanged(adapterPosition)
            }
        }
    }

    inner class SpecialOfferViewHolder(private val binding: ItemSpecialOfferBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CoffeItem) {
            binding.SpecialOfferTextView.text = item.milkType
            binding.imageSpecialOffer.setImageResource(item.imageRecId)
        }
    }


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


    fun updateData(newList: List<CoffeItem>) {
        if (newList.isEmpty()) return

        filteredList.clear()
        filteredList.addAll(newList)

        Log.d(
            "DEBUG",
            "Адаптер получает: ${filteredList.map { it.imageRecId }}"
        ) // 🔥 Логируем перед обновлением
        notifyDataSetChanged()
    }
}