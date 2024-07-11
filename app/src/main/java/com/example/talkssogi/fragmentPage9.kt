package com.example.talkssogi

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.Calendar

data class ImageResponse(
    val imageUrl: Int //서버 만들면 String으로 바꾸고 주소로 받아야함
)
class fragmentPage9 : Fragment() {
    private var selectedDate1: String? = null // 시작할 날짜 저장
    private var selectedDate2: String? = null // 끝날 날짜 저장

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_page9, container, false)

        val btnBack: ImageView = view.findViewById(R.id.imageView) //뒤로가기
        val buttonOpenCalendar1 = view.findViewById<Button>(R.id.button_open_calendar1)
        val buttonOpenCalendar2 = view.findViewById<Button>(R.id.button_open_calendar2)
        val searchSpinner: Spinner = view.findViewById(R.id.search_spinner)
        val resultsSpinner: Spinner = view.findViewById(R.id.results_spinner)
        val searchbtn : ImageButton = view.findViewById(R.id.create) //검색
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView_graph) //이미지 넣을 리싸이클러뷰
        recyclerView.layoutManager = LinearLayoutManager(context)

        // 뒤로가기 이미지 리스너
        btnBack.setOnClickListener {
            // 프래그먼트 매니저를 통해 뒤로 가기 동작
            requireActivity().supportFragmentManager.popBackStack()
        }

        buttonOpenCalendar1.setOnClickListener {
            showDatePicker(buttonOpenCalendar1)
        }

        buttonOpenCalendar2.setOnClickListener {
            showDatePicker(buttonOpenCalendar2)
        }

        // 검색 버튼 클릭 리스너 설정
        searchbtn.setOnClickListener {
            // 스피너에 선택되어 있는 항목 저장
            val selectedSearchItem = searchSpinner.selectedItem.toString()
            val selectedResultsItem = resultsSpinner.selectedItem.toString()

            // 서버에 이미지 요청하기
            // Retrofit을 사용한 서버 요청
            //mainViewModel.fetchImages(selectedSearchItem, selectedResultsItem))
            // 서버에서 이미지를 url로 받아서 어댑터에 넣기(?)
//            val imageUrl = "android.resource://${requireContext().packageName}/${R.drawable.test_img_page9}" // 서버 구축 전 테스트 사진
//            val testImageResponse = ImageResponse(imageUrl)
//            // RecyclerViewAdapter_page9 초기화
//            val itemList = listOf(testImageResponse)
//            val adapter = RecyclerViewAdapter_page9(itemList)
//            recyclerView.adapter = adapter

            // 일단 테스트용 코드
            val imageUrl = R.drawable.test_img_page9 // 이미지 리소스 ID
            val testImageResponse = ImageResponse(imageUrl)
            val itemList = listOf(testImageResponse)
            val adapter = RecyclerViewAdapter(itemList)
            recyclerView.adapter = adapter

        }

        // 스피너 아이템 설정
        // 뷰모델에서 대화 참여자 이름 가져와서 저장하기
        val searchItems = arrayOf("전체", "1", "2")
        val searchAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, searchItems)
        searchAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        searchSpinner.adapter = searchAdapter

        val resultsItems = arrayOf("보낸 메시지 수 그래프", "활발한 시간대 그래프", "대화를 보내지 않은 날짜")
        val resultsAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, resultsItems)
        resultsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        resultsSpinner.adapter = resultsAdapter

        // 일단 테스트용 코드(버튼 누르기 전까지 보일 이미지)
        val imageUrl = R.drawable.phone // 이미지 리소스 ID
        val testImageResponse = ImageResponse(imageUrl)
        val itemList = listOf(testImageResponse)
        val adapter = RecyclerViewAdapter(itemList)
        recyclerView.adapter = adapter

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
}
