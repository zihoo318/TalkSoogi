package com.example.talkssogi

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.Calendar

class fragmentPage9 : Fragment() {

    private var selectedDate1: String? = null // 시작할 날짜 저장
    private var selectedDate2: String? = null // 끝날 날짜 저장
    var viewModel: MyViewModel? = null // 공유 뷰모델
    private val activityAnalysisViewModel: ActivityAnalysisViewModel by viewModels()
    private var crnum: Int = -1 // arguments로 받을 변수 저장할 변수 초기화
    private lateinit var loadingIndicator: ProgressBar // ProgressBar 선언

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            crnum = it.getInt("crnum", -1)
            Log.d("FragmentPage9", "프래그먼트에서 argument 받음 crnum: $crnum")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_page9, container, false)

        val btnBack: ImageView = view.findViewById(R.id.imageView) // 뒤로가기
        val buttonOpenCalendar1 = view.findViewById<Button>(R.id.button_open_calendar1)
        val buttonOpenCalendar2 = view.findViewById<Button>(R.id.button_open_calendar2)
        val searchSpinner: Spinner = view.findViewById(R.id.search_spinner)
        val resultsSpinner: Spinner = view.findViewById(R.id.results_spinner)
        val searchBtn: ImageButton = view.findViewById(R.id.create) // 검색
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView_graph) // 리싸이클러뷰
        loadingIndicator = view.findViewById(R.id.loadingIndicator) // ProgressBar 초기화

        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        // 뒤로가기 이미지 리스너
        btnBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        buttonOpenCalendar1.setOnClickListener {
            showDatePicker(buttonOpenCalendar1)
        }

        buttonOpenCalendar2.setOnClickListener {
            showDatePicker(buttonOpenCalendar2)
        }

        // 검색 버튼 클릭 리스너 설정
        searchBtn.setOnClickListener {
            val selectedSearchItem = searchSpinner.selectedItem.toString()
            val selectedResultsItem = resultsSpinner.selectedItem.toString()
            Log.d("FragmentPage9", "선택된 항목의 값을 확인 Search Item: $selectedSearchItem")
            Log.d("FragmentPage9", "선택된 항목의 값을 확인 Results Item: $selectedResultsItem")

            // 이전 결과를 가리기 위해 RecyclerView의 어댑터를 null로 설정
            recyclerView.adapter = null

            // 로딩 인디케이터 표시
            showLoadingIndicator()

            // 서버에 이미지 요청하기
            activityAnalysisViewModel.getActivityAnalysisImage(
                selectedDate1,
                selectedDate2,
                selectedSearchItem,
                selectedResultsItem,
                crnum
            )

            // LiveData를 관찰하여 이미지 URL을 업데이트
            activityAnalysisViewModel.imageUrls.observe(viewLifecycleOwner, { imageUrls ->
                Log.d("FragmentPage9", "이미지 생성 후에 변할 url변수의 옵저버 안에 들어옴")
                if (imageUrls.isNotEmpty()) {
                    // 새로운 이미지 리스트가 있으면 어댑터 설정
                    val adapter = Page9RecyclerViewAdapter(imageUrls)
                    recyclerView.adapter = adapter
                }
            })

            // 로딩 상태를 관찰하여 로딩 인디케이터 표시/숨기기
            activityAnalysisViewModel.loading.observe(viewLifecycleOwner, { isLoading ->
                if (!isLoading) {
                    hideLoadingIndicator()
                }
            })

            // 오류를 관찰하여 사용자에게 알림 및 로딩 인디케이터 숨기기
            activityAnalysisViewModel.error.observe(viewLifecycleOwner, { error ->
                if (error != null) {
                    // 오류 발생 시 사용자에게 알림
                    Toast.makeText(context, "서버 오류 발생: $error", Toast.LENGTH_LONG).show()
                }
            })
        }






        // 스피너 아이템 설정
        activityAnalysisViewModel.loadParticipants(crnum)
        activityAnalysisViewModel.participants.observe(viewLifecycleOwner, { participants ->
            val participantAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, participants)
            participantAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            searchSpinner.adapter = participantAdapter
            Log.d("FragmentPage9", "참여자 가져와서 바꿈")
        })

        val resultsItems = arrayOf("보낸 메시지 수 그래프", "활발한 시간대 그래프", "대화를 보내지 않은 날짜")
        val resultsAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, resultsItems)
        resultsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        resultsSpinner.adapter = resultsAdapter

        return view
    }

    private fun showDatePicker(button: Button) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = "${selectedYear}년 ${selectedMonth + 1}월 ${selectedDay}일"

                when (button.id) {
                    R.id.button_open_calendar1 -> {
                        selectedDate1 = selectedDate // 선택된 날짜 저장
                        button.text = selectedDate1
                    }
                    R.id.button_open_calendar2 -> {
                        selectedDate2 = selectedDate // 선택된 날짜 저장
                        button.text = selectedDate2
                    }
                }
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    // "분석 중" 상태를 표시하는 함수
    private fun showLoadingIndicator() {
        Log.d("FragmentPage9", "LoadingIndicator 작동")
        loadingIndicator.visibility = View.VISIBLE
    }

    // "분석 중" 상태를 숨기는 함수
    fun hideLoadingIndicator() {
        Log.d("FragmentPage9", "LoadingIndicator 꺼짐")
        loadingIndicator.visibility = View.GONE
    }
}
