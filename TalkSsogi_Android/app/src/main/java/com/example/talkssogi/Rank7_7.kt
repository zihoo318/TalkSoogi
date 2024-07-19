package com.example.talkssogi

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer

class Rank7_7 : Fragment() {

    private val rankingViewModel: RankingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.rank7_board, container, false)

        val search = view.findViewById<ImageView>(R.id.button_search)
        val textView2 = view.findViewById<TextView>(R.id.textView2)

        search.setOnClickListener {
            val intent = Intent(requireContext(), Page7_search_Activity::class.java)
            startActivity(intent)
        }

        // ViewModel 데이터 관찰
        rankingViewModel.basicRankingResults.observe(viewLifecycleOwner, Observer { results ->
            // "주제1"의 랭킹을 가져와 표시
            val rankingList = results["주제7"]
            rankingList?.let {
                val displayText = it.joinToString(separator = "\n") { name -> "순위: $name" }
                textView2.text = displayText
            }
        })

        // 데이터 가져오기 요청
        rankingViewModel.fetchRankingResults()

        return view
    }
}
