package com.example.talkssogi

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Page1Activity : AppCompatActivity() {

    // 사용자가 입력한 ID를 저장할 리스트
    private val usedIDs = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.page1_activity) // activity_page1.xml 레이아웃을 설정

        // XML 레이아웃에서 View 객체를 찾는다
        val imageView: ImageView = findViewById(R.id.imageView)
        val etID: EditText = findViewById(R.id.etID)
        val idConfirm: TextView = findViewById(R.id.IDConfirm)
        val idConfirm2: TextView = findViewById(R.id.IDConfirm2)
        val btnUploadFile: ImageButton = findViewById(R.id.btnUploadFile)

        // EditText에 입력된 텍스트의 변화를 감지하는 TextWatcher 추가
        etID.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val inputID = s.toString() // 입력된 텍스트를 문자열로 가져옴
                if (inputID.isNotEmpty()) {
                    if (usedIDs.contains(inputID)) {
                        // 입력된 ID가 사용 중인 ID 목록에 포함되어 있는 경우
                        idConfirm2.visibility = TextView.VISIBLE // ID가 사용 중임을 나타내는 메시지 표시
                        idConfirm.visibility = TextView.GONE // ID가 사용 가능한 경우의 메시지 숨김
                    } else {
                        // 입력된 ID가 사용 중인 ID 목록에 포함되어 있지 않은 경우
                        idConfirm.visibility = TextView.VISIBLE // ID가 사용 가능한 경우의 메시지 표시
                        idConfirm2.visibility = TextView.GONE // ID가 사용 중임을 나타내는 메시지 숨김
                    }
                } else {
                    // 입력 필드가 비어있는 경우
                    idConfirm.visibility = TextView.GONE // ID가 사용 가능한 경우의 메시지 숨김
                    idConfirm2.visibility = TextView.GONE // ID가 사용 중임을 나타내는 메시지 숨김
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // 입력이 변경되기 전의 동작을 정의 (현재는 사용하지 않음)
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // 입력이 변경되는 동안의 동작을 정의 (현재는 사용하지 않음)
            }
        })

        // "Upload File" 버튼 클릭 시 실행되는 리스너 설정
        btnUploadFile.setOnClickListener {
            val newID = etID.text.toString()
            if (newID.isNotEmpty() && !usedIDs.contains(newID)) {
                // 입력된 ID가 빈 값이 아니고, usedIDs 리스트에 포함되어 있지 않은 경우
                usedIDs.add(newID) // ID를 usedIDs 리스트에 추가
                etID.text.clear() // EditText의 텍스트를 비움
                idConfirm.visibility = TextView.GONE // ID가 사용 가능한 경우의 메시지를 숨김
                idConfirm2.visibility = TextView.GONE // ID가 사용 중임을 나타내는 메시지를 숨김
                // ID가 추가되었다는 메시지를 표시할 수 있음 (예: Toast)
                // Toast.makeText(this, "ID added", Toast.LENGTH_SHORT).show()
            }
        }

        // 기타 초기화 작업
        imageView.setImageResource(R.drawable.happy) // 이미지 뷰에 smile2 이미지 설정
    }
}
