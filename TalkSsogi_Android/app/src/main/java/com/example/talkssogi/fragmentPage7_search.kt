package com.example.talkssogi

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.talkssogi.databinding.FragmentPage7SearchBinding

class fragmentPage7_search : Fragment() {
    private var _binding: FragmentPage7SearchBinding? = null
    private val binding get() = _binding!!
    private val rankingViewModel: RankingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment using ViewBinding
        _binding = FragmentPage7SearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSearchView()
        observeViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // SearchView 설정
    private fun initSearchView() {
        val searchView = binding.search
        val searchTitleSecond = binding.searchTitleSecond

        // 기본적으로 검색창을 열어놓음
        searchView.isIconified = false

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(keyword: String?): Boolean {
                keyword?.let {
                    // 검색 버튼을 누르면 clearFocus() 호출하고, TextView 업데이트 및 데이터 로드
                    searchView.clearFocus()
                    searchTitleSecond.text = keyword
                    rankingViewModel.fetchSearchRankingResults(keyword)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // @TODO
                return true
            }
        })

        // SearchView에 포커스가 가면 검색창이 뜨게 설정
        searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                searchView.isIconified = false
            }
        }
    }

    private fun observeViewModel() {
        rankingViewModel.searchRankingResults.observe(viewLifecycleOwner, Observer { results ->
            // text가 비어있다면
            if (results.isEmpty()) {
                binding.rankingResult.text = "No results found"
            } else {
                val rankingList = results.values.flatten()
                val displayText = rankingList.joinToString(separator = "\n") { name -> "순위: $name" }
                binding.rankingResult.text = displayText
            }
        })

        rankingViewModel.error.observe(viewLifecycleOwner, Observer { error ->
            error?.let {
                Log.e("fragmentPage7_search", it)
            }
        })
    }
}
