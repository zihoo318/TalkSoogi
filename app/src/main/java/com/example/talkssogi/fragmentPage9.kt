package com.example.talkssogi

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import java.util.Calendar

class fragmentPage9 : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.page9, container, false)

        val buttonOpenCalendar = view.findViewById<Button>(R.id.button_open_calendar)

        buttonOpenCalendar.setOnClickListener {
            showDatePicker(buttonOpenCalendar)
        }

        return view
    }

    private fun showDatePicker(button: Button) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, day ->
                val selectedDate = "${year}년 ${month + 1}월 ${day}일"
                // 버튼 텍스트 변경
                button.text = selectedDate
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }
}
