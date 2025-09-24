package com.example.cafeapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cafeapp.R

// Fragment responsible for returning the user to the main screen (MenuScreenActivity).
class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Load the fragment layout containing the button/element to return to the main activity.
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

}