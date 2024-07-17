package com.example.talkssogi

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment

class fragmentPage5 : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.page5_fragment_activity, container, false)

        val btnBack: ImageView = view.findViewById(R.id.imageView) //뒤로가기
        val option1 = view.findViewById<TextView>(R.id.option_1)
        val option2 = view.findViewById<TextView>(R.id.option_2)
        val option4 = view.findViewById<TextView>(R.id.option_4)

        // 뒤로가기 이미지 리스너
        btnBack.setOnClickListener {
            // 프래그먼트 매니저를 통해 뒤로 가기 동작
            requireActivity().supportFragmentManager.popBackStack()
        }

        option1.setOnClickListener {
            (requireActivity() as FragmentActivity).replaceFragment(fragmentPage6())
        }

        option2.setOnClickListener {
            val intent = Intent(requireContext(), Page7Activity::class.java)
            startActivity(intent)
        }

        option4.setOnClickListener {
            (requireActivity() as FragmentActivity).replaceFragment(fragmentPage8())
        }

        return view
    }
}