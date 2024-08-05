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
        val messageCountResult: TextView = view.findViewById(R.id.messageCountResult)
        val zeroCountResult: TextView = view.findViewById(R.id.zeroCountResult)
        val hourlyCountResult: TextView = view.findViewById(R.id.hourlyCountResult)
        val btnGotoPage9: ImageView = view.findViewById(R.id.BtnGotoPage9)

        // ViewModel을 사용하여 데이터 설정
        viewModel.messageCountResult.observe(viewLifecycleOwner) { count ->
            messageCountResult.text = count.toString()
        }

        viewModel.zeroCountResult.observe(viewLifecycleOwner) { count ->
            zeroCountResult.text = count.toString()
        }

        viewModel.hourlyCountResult.observe(viewLifecycleOwner) { count ->
            hourlyCountResult.text = count.toString()
        }


        // 뒤로가기 버튼 클릭 리스너
        btnBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        // 프래그먼트9로 이동할 버튼 클릭 리스너
        btnGotoPage9.setOnClickListener {
            val fragment = fragmentPage9().apply {
                arguments = Bundle().apply {
                    putInt("crnum", crnum) // 채팅방 번호를 arguments에 추가
                    Log.d("Page9", "프래그먼트페이지8에서 argument로 넣은 crnum : $crnum")
                }
            }
            (requireActivity() as FragmentActivity).replaceFragment(fragment)
        }

        // ViewModel을 통해 데이터를 요청
        lifecycleScope.launch {
            try {
                viewModel.fetchActivityAnalysis(crnum) { result ->
                    // 결과 처리
                    // result가 List<String>이므로 이를 적절히 가공하여 UI에 반영
                    if (result.isNotEmpty()) {
                        // 예를 들어, 결과 리스트의 첫 번째 요소를 메시지 카운트로 사용
                        if (result.size > 0) {
                            messageCountResult.text = result[0]
                        }
                        // 두 번째 요소를 제로 카운트 날짜로 사용
                        if (result.size > 1) {
                            zeroCountResult.text = result[1]
                        }
                        // 세 번째 요소를 시간대 카운트로 사용
                        if (result.size > 2) {
                            hourlyCountResult.text = result[2]
                        }
                    } else {
                        // 결과가 비어있을 때의 처리
                        messageCountResult.text = "No data"
                        zeroCountResult.text = "No data"
                        hourlyCountResult.text = "No data"
                    }
                }
            } catch (e: Exception) {
                Log.e("Page8", "Error fetching activity analysis", e)
                // 에러 메시지 설정
                messageCountResult.text = "Error"
                zeroCountResult.text = "Error"
                hourlyCountResult.text = "Error"
            }
        }

        return view
    }
}
