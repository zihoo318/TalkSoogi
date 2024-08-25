package com.example.talkssogi

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RankingRecyclerViewAdapter(private var rankingList: List<String>) : RecyclerView.Adapter<RankingRecyclerViewAdapter.RankingViewHolder>() {

    inner class RankingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rankingResut: TextView = itemView.findViewById(R.id.ranking_result)


        fun bind(ranking: String) {
            rankingResut.text = ranking
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ranking, parent, false)
        return RankingViewHolder(view)
    }

    override fun onBindViewHolder(holder: RankingViewHolder, position: Int) {
        try {
            holder.bind(rankingList[position])
        } catch (e: Exception) {
            Log.e("RecyclerView", "Error in onBindViewHolder: ${e.message}")
        }
    }

    override fun getItemCount(): Int = rankingList.size

    fun updateData(newItems: List<String>) {
        this.rankingList = newItems ?: emptyList()  // Null-safe 처리
        notifyDataSetChanged()
    }
}