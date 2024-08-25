package com.example.talkssogi

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class Page7_search_Activity : AppCompatActivity() {
    private var crnum: Int = -1 // 기본값 설정

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.page7_search)

        // Intent로부터 crnum 값을 받아옴
        crnum = intent.getIntExtra("crnum", -1)
        Log.d("Page7_search_Activity", "Page7_search_Activity crnum 값: $crnum")

        if (savedInstanceState == null) {
            val fragment = fragmentPage7_search.newInstance(crnum)
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
        }

        // undo_button 클릭 시 Page7Activity로 이동하도록 설정
        findViewById<ImageView>(R.id.undo_button).setOnClickListener {
            val intent = Intent(this, Page7Activity::class.java)
            intent.putExtra("crnum", crnum) // 필요한 경우 데이터 전달
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP // 기존의 Page7Activity 인스턴스를 재사용하고 그 위의 액티비티들을 제거
            startActivity(intent)
            finish() // 현재 액티비티를 종료하여 뒤로 가기 버튼을 눌렀을 때 다시 이 화면으로 돌아오지 않도록 설정
        }
    }
}