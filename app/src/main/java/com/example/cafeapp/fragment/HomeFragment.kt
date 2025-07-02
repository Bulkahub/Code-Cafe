package com.example.cafeapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cafeapp.R

// Фрагмент, отвечающий за возвращение пользователя на главный экран (MenuScreenActivity).
class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Загружаем макет фрагмента, содержащий кнопку/элемент возврата на главную активити.
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

}