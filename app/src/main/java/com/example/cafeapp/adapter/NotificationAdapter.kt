package com.example.cafeapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cafeapp.R
import com.example.cafeapp.dataclass.NotificationData

// Adapter for displaying notifications in RecyclerView.
class NotificationAdapter :
    ListAdapter<NotificationData, NotificationAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_notification, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    // ViewHolder — binds NotificationData to layout.
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        // UI elements — icon and message text.
        private val icon = itemView.findViewById<ImageView>(R.id.ic_info)
        private val text = itemView.findViewById<TextView>(R.id.messageTextNotif)

        // Method to display notification content.
        fun bind(note: NotificationData) {
            text.text = note.message // Sets the notification message text.

            // Selects icon based on notification type.
            icon.setImageResource(
                when (note.type) {
                    NotificationData.Type.SUCCESS -> R.drawable.ic_check
                    NotificationData.Type.ERROR -> R.drawable.ic_error
                    else -> R.drawable.ic_info
                }
            )
        }
    }

    // Callback for comparing notifications when updating the list.
    companion object {
        val DiffCallback = object : DiffUtil.ItemCallback<NotificationData>() {

            // Compare IDs — used to determine if the item has changed.
            override fun areItemsTheSame(oldItem: NotificationData, newItem: NotificationData) =
                oldItem.id == newItem.id

            // Compare full content of the notification.
            override fun areContentsTheSame(oldItem: NotificationData, newItems: NotificationData) =
                oldItem == newItems
        }
    }
}