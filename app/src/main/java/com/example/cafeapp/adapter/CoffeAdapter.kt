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

// Adapter for displaying the list of coffee items and special offers.
class CoffeAdapter(
    private val coffeList: MutableList<CoffeItem> = mutableListOf(),
    private val cartList: MutableList<CoffeItem>,// List of items already added to the cart.
    private val onAddClick: (CoffeItem) -> Unit // Callback when the "add" icon is clicked.
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_COFFEE = 0   // Regular item type.
        private const val VIEW_TYPE_SPECIAL = 1    // Special offer item type.
    }

    // Filtered list displayed in the RecyclerView.
    private var filteredList = coffeList.toMutableList()

    // Return item type — used to choose the appropriate ViewHolder.
    override fun getItemViewType(position: Int): Int {
        return if (filteredList[position].name == "Special Offer") VIEW_TYPE_SPECIAL else VIEW_TYPE_COFFEE
    }

    // Create the appropriate ViewHolder depending on the item type.
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

    // Bind data to the ViewHolder depending on the item type.
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

    // Number of items displayed.
    override fun getItemCount() = filteredList.size

    // ViewHolder for regular coffee items.
    inner class CoffeViewHolder(private val binding: ItemCoffeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CoffeItem) {
            // Bind data to UI.
            binding.textViewCofeeName.text = item.name
            binding.textViewCofFeMilk.text = item.milkType
            binding.textViewPrice.text = item.price
            binding.imageViewCoffe.setImageResource(item.imageRecId)

            // Adjust add button transparency if item is already in the cart.
            if (cartList.contains(item)) {
                binding.imageViewAdd.animate().alpha(0.5f).setDuration(300)
            } else {
                binding.imageViewAdd.animate().alpha(1.0f).setDuration(300)
            }

            // Handle click on the add icon.
            binding.imageViewAdd.setOnClickListener {
                onAddClick(item)
                notifyItemChanged(adapterPosition) // Refresh current position.

                // Navigate to detailed coffee information.
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

    // ViewHolder for the special offer block.
    inner class SpecialOfferViewHolder(private val binding: ItemSpecialOfferBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CoffeItem) {
            binding.SpecialOfferTextView.text = item.milkType
            binding.imageSpecialOffer.setImageResource(item.imageRecId)
        }
    }

    // Method for filtering the coffee list.
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

    // Update the displayed coffee list and notify the adapter of changes.
    fun updateData(newList: List<CoffeItem>) {
        if (newList.isEmpty()) return // Skip empty updates.

        // Clear previous data and add new items.
        filteredList.clear()
        filteredList.addAll(newList)

        // Notify adapter of full data refresh.
        notifyDataSetChanged()
    }
}