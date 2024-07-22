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

class fragmentPage8 : Fragment() {
    //
    private val rankingViewModel: RankingViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 이 프래그먼트의 레이아웃을 인플레이트하여 반환
        val view = inflater.inflate(R.layout.fragment_page8, container, false)

        // 레이아웃에서 UI 요소를 초기화
        val btnBack: ImageView = view.findViewById(R.id.imageView) //뒤로가기
        val imageView1: ImageView = view.findViewById(R.id.imageView)
        val imageView2: ImageView = view.findViewById(R.id.imgResult)
        val textView: TextView = view.findViewById(R.id.txtViewResult)
        val imageView3: ImageView = view.findViewById(R.id.BtnGotoPage9)

        // 뒤로가기 이미지 리스너
        btnBack.setOnClickListener {
            // 프래그먼트 매니저를 통해 뒤로 가기 동작
            requireActivity().supportFragmentManager.popBackStack()
        }

        //프래그먼트9로 이동할 버튼 리스너
        imageView3.setOnClickListener{
            (requireActivity() as FragmentActivity).replaceFragment(fragmentPage9())
        }


        // 해결 안됨
        // ViewModel 데이터 관찰
        rankingViewModel.activityAnalysis.observe(viewLifecycleOwner, Observer { results ->
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

        // 데이터 가져오기 요청
        rankingViewModel.fetchRankingResults()

        return view
        // 예시 설정: 필요한 경우 텍스트나 이미지를 설정
        // textView.text = "동적 콘텐츠"
        // imageView1.setImageResource(R.drawable.some_image)
        // imageView2.setImageResource(R.drawable.some_image)
        // imageView3.setImageResource(R.drawable.some_image)

        // 프래그먼트의 뷰를 반환

    }
}
