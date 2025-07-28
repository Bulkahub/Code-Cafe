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

// Адаптер для отображения уведомлений в RecyclerView.
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

    // ViewHolder — связывает данные NotificationData с layout.
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        // Элементы интерфейса — иконка и текст сообщения.
        private val icon = itemView.findViewById<ImageView>(R.id.ic_info)
        private val text = itemView.findViewById<TextView>(R.id.messageTextNotif)

        // Метод для отображения содержимого уведомления.
        fun bind(note: NotificationData) {
            text.text = note.message // Устанавливает текст уведомления.

            // Выбирает иконку в зависимости от типа уведомления.
            icon.setImageResource(
                when (note.type) {
                    NotificationData.Type.SUCCESS -> R.drawable.ic_check
                    NotificationData.Type.ERROR -> R.drawable.ic_error
                    else -> R.drawable.ic_info
                }
            )
        }
    }

    // Callback для сравнения уведомлений при обновлении списка.
    companion object {
        val DiffCallback = object : DiffUtil.ItemCallback<NotificationData>() {

            // Сравниваем ID — используется для определения, изменился ли элемент.
            override fun areItemsTheSame(oldItem: NotificationData, newItem: NotificationData) =
                oldItem.id == newItem.id

            // Сравниваем полное содержимое уведомления.
            override fun areContentsTheSame(oldItem: NotificationData, newItems: NotificationData) =
                oldItem == newItems
        }
    }
}