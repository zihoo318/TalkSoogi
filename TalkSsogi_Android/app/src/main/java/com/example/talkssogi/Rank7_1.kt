package com.example.talkssogi

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction

class Rank7_1 : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.rank7_board, container, false)

        val search = view.findViewById<ImageView>(R.id.button_search)


        search.setOnClickListener {
            val intent = Intent(requireContext(), Page7_search_Activity::class.java)
            startActivity(intent)
        }

        return view
    }
}
