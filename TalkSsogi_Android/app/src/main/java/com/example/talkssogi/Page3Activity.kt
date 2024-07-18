package com.example.talkssogi

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Page3Activity : AppCompatActivity() {

    companion object {
        const val REQUEST_CODE_SELECT_FILE = 1
    }

    private lateinit var tvPeople: TextView
    private lateinit var etPeopleCount: EditText
    private lateinit var btnPeople: ImageButton
    private lateinit var tvSelectedFile: TextView
    private lateinit var imageView: ImageView
    private lateinit var textView: TextView
    private lateinit var imageView3: ImageView
    private lateinit var imageView2: ImageView
    private lateinit var btnUploadFile: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.page3_activity)

        // UI 요소의 참조를 가져옵니다.
        tvPeople = findViewById(R.id.tvPeople)
        etPeopleCount = findViewById(R.id.etPeopleCount)
        btnPeople = findViewById(R.id.btnPeople)
        tvSelectedFile = findViewById(R.id.tvSelectedFile)
        imageView = findViewById(R.id.imageView)
        textView = findViewById(R.id.textView)
        imageView3 = findViewById(R.id.imageView3)
        imageView2 = findViewById(R.id.imageView2)
        btnUploadFile = findViewById(R.id.btnUploadFile)

        // null 체크 추가
        checkForNulls()

        // tvSelectedFile 클릭 이벤트 설정
        tvSelectedFile.setOnClickListener {
            openFileChooser()
        }

        // 파일 업로드 버튼 클릭 이벤트 설정
        btnUploadFile.setOnClickListener {
            openFileChooser()
        }

        // 확인 버튼 클릭 이벤트 설정
        btnPeople.setOnClickListener {
            handlePeopleCount()
        }

        // 기타 초기화 작업 수행
        imageView.setImageResource(R.drawable.smile)
        textView.text = "분석할 파일을 업로드하세요."
    }

    private fun checkForNulls() {
        if (!::tvPeople.isInitialized) Log.e("Page3Activity", "tvPeople is null")
        if (!::etPeopleCount.isInitialized) Log.e("Page3Activity", "etPeopleCount is null")
        if (!::btnPeople.isInitialized) Log.e("Page3Activity", "btnPeople is null")
        if (!::tvSelectedFile.isInitialized) Log.e("Page3Activity", "tvSelectedFile is null")
        if (!::imageView.isInitialized) Log.e("Page3Activity", "imageView is null")
        if (!::textView.isInitialized) Log.e("Page3Activity", "textView is null")
        if (!::imageView3.isInitialized) Log.e("Page3Activity", "imageView3 is null")
        if (!::imageView2.isInitialized) Log.e("Page3Activity", "imageView2 is null")
        if (!::btnUploadFile.isInitialized) Log.e("Page3Activity", "btnUploadFile is null")
    }

    private fun handlePeopleCount() {
        val peopleCount = etPeopleCount.text.toString()
        if (peopleCount.isNotEmpty()) {
            // 인원 수 처리 로직 추가
            tvPeople.text = "입력된 인원 수: $peopleCount"
        } else {
            tvPeople.text = "인원 수를 입력해주세요."
        }
    }

    private fun openFileChooser() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "*/*"
            addCategory(Intent.CATEGORY_OPENABLE)
        }
        startActivityForResult(intent, REQUEST_CODE_SELECT_FILE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SELECT_FILE && resultCode == Activity.RESULT_OK) {
            val fileUri = data?.data
            tvSelectedFile.text = fileUri?.path ?: "파일 선택 실패"

            // 파일 업로드 완료 후 이전 Activity2로 복귀하는 코드
            val intent = Intent()
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    private fun analyzeFile() {
        tvSelectedFile.text = "파일 분석 중..."
        // 실제 분석 로직을 추가하세요.
        // 분석이 완료되면 결과를 표시합니다.
        tvSelectedFile.text = "파일 분석 완료"
    }
}
