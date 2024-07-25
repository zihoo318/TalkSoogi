package com.example.talkssogi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer

class FragmentPage8 : Fragment() {

    private val viewModel: ActivityAnalysisViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_page8, container, false)

        // UI 요소 초기화
        val btnBack: ImageView = view.findViewById(R.id.imageView) // 뒤로가기
        val imageView1: ImageView = view.findViewById(R.id.imageView)
        val imageView2: ImageView = view.findViewById(R.id.imgResult)
        val textView: TextView = view.findViewById(R.id.txtViewResult)
        val imageView3: ImageView = view.findViewById(R.id.BtnGotoPage9)

        // 뒤로가기 버튼 클릭 리스너
        btnBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        // 프래그먼트9로 이동할 버튼 클릭 리스너
        imageView3.setOnClickListener {
            (requireActivity() as FragmentActivity).replaceFragment(fragmentPage9())
        }

        // ViewModel 데이터 관찰
        viewModel.activityAnalysis.observe(viewLifecycleOwner, Observer { results ->
            val displayText = buildString {
                results.forEach { (key, value) ->
                    append("$key:\n")
                    value.forEach { item ->
                        append("- $item\n")
                    }
                    append("\n")
                }
            }
            textView.text = displayText
        })

        // API 호출
        val crnum = 1 // 실제 값으로 설정하세요
        viewModel.fetchActivityAnalysis(crnum)

        return view
    }
}
