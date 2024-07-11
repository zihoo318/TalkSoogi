package com.example.talkssogi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class fragmentPage5 : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.page5_fragment_activity, container, false)

        val option1 = view.findViewById<TextView>(R.id.option_1)
        val option2 = view.findViewById<TextView>(R.id.option_2)
        val option4 = view.findViewById<TextView>(R.id.option_4)

        option1.setOnClickListener {
            (requireActivity() as FragmentActivity).replaceFragment(fragmentPage6())
        }

        option2.setOnClickListener {
            (requireActivity() as FragmentActivity).replaceFragment(fragmentPage7())
        }

        option4.setOnClickListener {
            (requireActivity() as FragmentActivity).replaceFragment(fragmentPage8())
        }

        return view
    }
}
