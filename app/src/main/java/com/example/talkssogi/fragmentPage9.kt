package com.example.talkssogi

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.fragment.app.Fragment
import java.util.Calendar

class fragmentPage9 : Fragment() {
    // private lateinit var viewModel: MyViewModel // 스피너의 항목을 채팅방 구성원 이름 가져와서 출력
    private var selectedDate1: String? = null // 시작할 날짜 저장
    private var selectedDate2: String? = null // 끝날 날짜 저장

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_page9, container, false)

        val buttonOpenCalendar1 = view.findViewById<Button>(R.id.button_open_calendar1)
        val buttonOpenCalendar2 = view.findViewById<Button>(R.id.button_open_calendar2)
        val searchSpinner: Spinner = view.findViewById<Spinner>(R.id.search_spinner)
        val resultsSpinner: Spinner = view.findViewById<Spinner>(R.id.results_spinner)

        buttonOpenCalendar1.setOnClickListener {
            showDatePicker(buttonOpenCalendar1)
        }

        buttonOpenCalendar2.setOnClickListener {
            showDatePicker(buttonOpenCalendar2)
        }

        val searchItems = arrayOf("전체", "1", "2")
        // 뷰모델에서 대화 참여자 이름 가져와서 저장
        val searchAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, searchItems)
        searchAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        searchSpinner.adapter = searchAdapter

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
}
