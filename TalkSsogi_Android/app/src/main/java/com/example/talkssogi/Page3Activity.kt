package com.example.talkssogi

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.activity.viewModels


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
    private lateinit var pot: ImageView
    private lateinit var speech_bubble: ImageView
    private lateinit var btnUploadFile: ImageButton
    //추가
    private val viewModel: MyViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.page3_activity)

        // UI 요소의 참조를 가져옵니다.
        tvPeople = findViewById(R.id.tvPeople)
        etPeopleCount = findViewById(R.id.etPeopleCount)
        tvSelectedFile = findViewById(R.id.tvSelectedFile)
        imageView = findViewById(R.id.undo_button)
        textView = findViewById(R.id.title_analyze)
        speech_bubble = findViewById(R.id.analyze_speech)
        pot = findViewById(R.id.pot_page3)
        btnUploadFile = findViewById(R.id.btnUploadFile)

        // null 체크 추가
        checkForNulls()

        // tvSelectedFile 클릭 이벤트 설정
        tvSelectedFile.setOnClickListener {
            openFileChooser()
        }

        // 파일 업로드 버튼 클릭 이벤트 설정
        btnUploadFile.setOnClickListener {
            handlePeopleCount()
        }

        // TextWatcher 설정(인원수가 수정될 때마다 바로 업데이트)
        etPeopleCount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                handlePeopleCount()
            }
        })

        // 기타 초기화 작업 수행
        imageView.setImageResource(R.drawable.smile)
    }

    private fun checkForNulls() {
        if (!::tvPeople.isInitialized) Log.e("Page3Activity", "tvPeople is null")
        if (!::etPeopleCount.isInitialized) Log.e("Page3Activity", "etPeopleCount is null")
        if (!::btnPeople.isInitialized) Log.e("Page3Activity", "btnPeople is null")
        if (!::tvSelectedFile.isInitialized) Log.e("Page3Activity", "tvSelectedFile is null")
        if (!::imageView.isInitialized) Log.e("Page3Activity", "imageView is null")
        if (!::textView.isInitialized) Log.e("Page3Activity", "textView is null")
        if (!::pot.isInitialized) Log.e("Page3Activity", "imageView3 is null")
        if (!::speech_bubble.isInitialized) Log.e("Page3Activity", "imageView2 is null")
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
    //업로드 버튼 클릭시 파일과 인원수 뷰모델로 넘어감
    private fun uploadFileAndPeopleCount() {
        val peopleCount = etPeopleCount.text.toString().toIntOrNull() ?: 0
        val fileUri = tvSelectedFile.text.toString()

        viewModel.setHeadCountAndFile(peopleCount, fileUri)
    }
}
