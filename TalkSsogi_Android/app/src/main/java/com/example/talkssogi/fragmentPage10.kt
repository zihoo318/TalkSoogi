package com.example.app

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.talkssogi.ActivityAnalysisViewModel
import com.example.talkssogi.R

class fragmentPage10 : Fragment() {

    private val viewModel: ActivityAnalysisViewModel by viewModels()
    private lateinit var searchView: SearchView
    private var crnum: Int = -1 // 기본값을 -1로 설정

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_page10, container, false)

        // Initialize UI components
        val btnBack: ImageView = view.findViewById(R.id.imageView) // 뒤로가기 버튼
        searchView = view.findViewById(R.id.searchView)

        // 뒤로가기 버튼 클릭 리스너
        btnBack.setOnClickListener {
            // 프래그먼트 매니저를 통해 뒤로 가기 동작
            requireActivity().supportFragmentManager.popBackStack()
        }

        // Setup SearchView listener
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get crnum from arguments
        crnum = arguments?.getInt("crnum") ?: -1
        Log.d("fragmentPage10", "crnum in fragmentPage10: $crnum") // crnum 값 로그 출력
    }

    private fun performSearch(result: String) {
        val fragment = fragmentPage10Result().apply {
            arguments = Bundle().apply {
                putString("result", result)
            }
        }
        childFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}