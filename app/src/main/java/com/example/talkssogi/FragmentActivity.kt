package com.example.talkssogi

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

// 액티비티에 page5를 기본으로 올리고 버튼을 누르면 다른 페이지의 프래그먼트로 바뀜

class FragmentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment_container)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragmentPage5())
                .commit()
        }
    }

    fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)  // 옵션 1, 2, 4 클릭 후 뒤로 가기 버튼을 눌렀을 때 이전 상태로 되돌리기 위해 백 스택에 추가
            .commit()
    }
}
