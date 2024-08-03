package com.example.talkssogi

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class fragmentPage8 : Fragment() {

    private val viewModel: ActivityAnalysisViewModel by viewModels()
    private var crnum: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            crnum = it.getInt("crnum", -1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_page8, container, false)

        // arguments에서 채팅방 번호를 가져옴
        crnum = arguments?.getInt("crnum", -1) ?: -1
        Log.d("Page9", "프래그먼트 페이지8이 출력!! 받은 crnum: $crnum")

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
            val fragment = fragmentPage9().apply {
                arguments = Bundle().apply {
                    putInt("crnum", crnum) // 채팅방 번호를 arguments에 추가
                    Log.d("Page9", "프래그먼트페이지8에서 argument로 넣은 crnum : $crnum")
                }
            }
            (requireActivity() as FragmentActivity).replaceFragment(fragment)
        }

        // API 호출 및 결과 가공 후 TextView에 출력
        lifecycleScope.launch {
            try {
                // API 호출
                val results = viewModel.fetchActivityAnalysis(crnum)

                // 결과 가공
                val displayText = buildString {
                    results.forEach { (key, value) ->
                        append("$key:\n")
                        value.forEach { item ->
                            append("- $item\n")
                        }
                        append("\n")
                    }
                }

                // 결과 출력
                textView.text = displayText

            } catch (e: Exception) {
                textView.text = "데이터를 가져오는 데 실패했습니다."
            }
        }

        return view
    }
}
