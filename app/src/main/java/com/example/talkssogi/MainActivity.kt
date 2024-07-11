package com.example.talkssogi

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

//main은 들어오는 화면..?
//1번이 main이 될 예정인데 일단 테스트랑 틀을 위해 따로 액티비티 만들어둘게요
//지금은 MainActivity랑 activity_main은 테스트용으로 쓰입니당

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Page2Activity로 이동하는 버튼 설정
        val buttonOpenPage2 = findViewById<Button>(R.id.button_open_page2)
        buttonOpenPage2.setOnClickListener {
            val intent = Intent(this, Page2Activity::class.java)
            startActivity(intent)
        }

        ////////////page5로 가기/////////
        val buttonOpenPage9 = findViewById<Button>(R.id.button_open_page5)
        buttonOpenPage9.setOnClickListener {
            val intent = Intent(this, FragmentActivity::class.java)
            startActivity(intent)
        }
        // 테스트를 위해 본인이 필요한 곳으로 가는 버튼을 만들어서 실행해보기!!!

    }
}