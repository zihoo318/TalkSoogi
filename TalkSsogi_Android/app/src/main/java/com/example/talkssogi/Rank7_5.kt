package com.example.talkssogi

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class Rank7_5 : Fragment() {

    private val rankingViewModel: RankingViewModel by viewModels()
    private var crnum: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.rank7_board, container, false)
        crnum = arguments?.getInt("crnum") ?: -1
        Log.d("fragmentPage5", "crnum in fragmentPage5: $crnum")

        val search = view.findViewById<ImageView>(R.id.button_search)
        val rankingRecyclerView = view.findViewById<RecyclerView>(R.id.ranking_result)

        search.setOnClickListener {
            val intent = Intent(requireContext(), Page7_search_Activity::class.java).apply {
                putExtra("crnum", crnum)
            }
            startActivity(intent)
        }

        rankingRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        rankingViewModel.basicRankingResults.observe(viewLifecycleOwner, Observer { results ->
            val messageRankings = results["오타"]
            messageRankings?.let {
                val displayList = it.entries
                    .sortedByDescending { entry -> entry.value }
                    .mapIndexed { index, entry -> "${index + 1}등: ${entry.key}  ${entry.value}개" }
                    .toList()

                val adapter = RankingRecyclerViewAdapter(displayList)
                rankingRecyclerView.adapter = adapter
            }
        })

        viewLifecycleOwner.lifecycleScope.launch {
            rankingViewModel.fetchBasicRankingResults(crnum)
        }

        return view
    }

    companion object {
        private const val ARG_CRNUM = "crnum"

        fun newInstance(crnum: Int): Rank7_5 {
            val fragment = Rank7_5()
            val args = Bundle()
            args.putInt(ARG_CRNUM, crnum)
            fragment.arguments = args
            return fragment
        }
    }
}