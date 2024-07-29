package com.example.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.talkssogi.R

// 검색 기능을 처리하고 검색 결과를 표시하는 프래그먼트
class fragmentNewPage : Fragment() {

    private lateinit var searchView: SearchView
    private lateinit var senderResult: TextView

    // 이 프래그먼트의 레이아웃을 인플레이트함
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_newpage, container, false)
    }

    // 뷰가 생성된 후 호출됨
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ID로 뷰를 찾음
        searchView = view.findViewById(R.id.searchView)
        senderResult = view.findViewById(R.id.SenderResult)

        // 검색 뷰에 리스너를 설정하여 검색 쿼리를 처리함
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            // 사용자가 검색 쿼리를 제출했을 때 호출됨
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    performSearch(it) // 검색을 수행하고 결과를 업데이트함
                }
                return true
            }

            // 검색 쿼리 텍스트가 변경될 때 호출됨
            override fun onQueryTextChange(newText: String?): Boolean {
                return false // 텍스트 변경은 여기서 처리하지 않음
            }
        })
    }

    // 검색을 수행하고 검색 결과를 TextView에 업데이트하는 함수
    private fun performSearch(query: String) {
        senderResult.text = "Search result for \"$query\""
    }
}
