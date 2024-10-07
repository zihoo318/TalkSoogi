package com.example.app

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.talkssogi.R

class fragmentPage10Result : Fragment() {

    private lateinit var senderResultTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_page10_result, container, false)

        // TextView 초기화
        senderResultTextView = view.findViewById(R.id.SenderResult)

        // arguments에서 결과값 받기
        val result = arguments?.getString("result")
        Log.d("fragmentPage10Result", "Result: $result")

        // TextView에 결과값 설정
        senderResultTextView.text = result ?: "결과를 불러올 수 없습니다."

        return view
    }
}
