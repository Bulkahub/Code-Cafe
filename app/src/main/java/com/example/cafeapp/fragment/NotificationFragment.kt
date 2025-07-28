package com.example.cafeapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cafeapp.R
import com.example.cafeapp.adapter.NotificationAdapter
import com.example.cafeapp.view.NotificationViewModel
import dagger.hilt.android.AndroidEntryPoint

/** Фрагмент, отображающий список уведомлений пользователя.*/
@AndroidEntryPoint
class NotificationFragment : Fragment(R.layout.notification_fragment) {

    // ViewModel с уведомлениями, привязанная к жизненному циклу фрагмента.
    private val viewModel: NotificationViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // Инициализация адаптера для отображения уведомлений.
        val adapterNotif = NotificationAdapter()

        // Получаем ссылку на RecyclerView и устанавливаем менеджер для вертикального списка.
        val recyclerViewNotif = view.findViewById<RecyclerView>(R.id.recyclerViewNotification)
        recyclerViewNotif.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewNotif.adapter = adapterNotif

        // Подписка на поток уведомлений из ViewModel.
        lifecycleScope.launchWhenStarted {
            viewModel.notifications.collect { list ->
                // Обновляем адаптер, отображая свежие уведомления в обратном порядке.
                adapterNotif.submitList(list.reversed())
            }
        }
    }
}