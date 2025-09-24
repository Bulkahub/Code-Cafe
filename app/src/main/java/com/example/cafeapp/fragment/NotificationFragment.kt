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

/** Fragment displaying the user's list of notifications.*/
@AndroidEntryPoint
class NotificationFragment : Fragment(R.layout.notification_fragment) {

    // ViewModel with notifications, tied to the fragment's lifecycle.
    private val viewModel: NotificationViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // Initialize adapter for displaying notifications.
        val adapterNotif = NotificationAdapter()

        // Get reference to RecyclerView and set layout manager for vertical list.
        val recyclerViewNotif = view.findViewById<RecyclerView>(R.id.recyclerViewNotification)
        recyclerViewNotif.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewNotif.adapter = adapterNotif

        // Subscribe to the notification flow from ViewModel.
        lifecycleScope.launchWhenStarted {
            viewModel.notifications.collect { list ->
                // Update adapter to show fresh notifications in reverse order.
                adapterNotif.submitList(list.reversed())
            }
        }
    }
}