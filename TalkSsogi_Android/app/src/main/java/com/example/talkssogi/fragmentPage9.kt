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
import androidx.lifecycle.ViewModelProvider

data class ImageResponse(
    val imageUrl: Int //서버 만들면 String으로 바꾸고 주소로 받아야함
)
class fragmentPage9 : Fragment() {
    private var selectedDate1: String? = null // 시작할 날짜 저장
    private var selectedDate2: String? = null // 끝날 날짜 저장
    lateinit var viewModel: MyViewModel

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
            val selectedSearchItem = searchSpinner.selectedItem.toString()
            val selectedResultsItem = resultsSpinner.selectedItem.toString()

            // 서버에 이미지 요청하기
            val searchData = Page9SearchData(
                selectedDate1,
                selectedDate2,
                selectedSearchItem,
                selectedResultsItem
            )

            viewModel.getActivityAnalysisImage(searchData)

            // 이미지 URL을 LiveData로 관찰하여 업데이트
            viewModel.imageUrls.observe(viewLifecycleOwner, { imageResponses ->
                // List<ImageResponse>를 List<ImageURL>로 변환
                val imageURLs = imageResponses.map { ImageURL(it.imageUrl) }
                val adapter = RecyclerViewAdapter(imageURLs)
                recyclerView.adapter = adapter
            })
        }

        // 스피너 아이템 설정
        // 대화방 참가자 목록 로드 및 스피너 업데이트
        val chatRoomId = 1 // 실제 채팅방 ID로 변경 필요
        viewModel.loadParticipants(chatRoomId)
        viewModel.participants.observe(viewLifecycleOwner, { participants ->
            // 참가자 목록을 스피너의 아이템으로 설정
            val participantAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, participants)
            participantAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            searchSpinner.adapter = participantAdapter
        })

        val resultsItems = arrayOf("보낸 메시지 수 그래프", "활발한 시간대 그래프", "대화를 보내지 않은 날짜")
        val resultsAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, resultsItems)
        resultsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        resultsSpinner.adapter = resultsAdapter

//        // 일단 테스트용 코드(버튼 누르기 전까지 보일 이미지)
//        val imageUrl = R.drawable.phone // 이미지 리소스 ID
//        val testImageResponse = ImageResponse(imageUrl)
//        val itemList = listOf(testImageResponse)
//        val adapter = RecyclerViewAdapter(itemList)
//        recyclerView.adapter = adapter

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
