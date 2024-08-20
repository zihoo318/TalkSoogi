package com.example.talkssogi

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class Page7Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.page7)

        // Fragment 설정
        if (savedInstanceState == null) {
            val fragment = fragmentPage7().apply {
                arguments = Bundle().apply {
                    putInt("crnum", intent.getIntExtra("crnum", -1))
                }
            }
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
        }

        // 상단 이미지뷰 클릭 리스너 설정
        findViewById<ImageView>(R.id.imageView).setOnClickListener {
            // 뒤로가기 동작 또는 다른 로직
            onBackPressed()
        }
    }
}
