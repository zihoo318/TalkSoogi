package com.yourpackage.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.talkssogi.Rank7_1
import com.example.talkssogi.Rank7_2
import com.example.talkssogi.Rank7_3
import com.example.talkssogi.Rank7_4

class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    private val fragments = listOf(Rank7_1(), Rank7_2(), Rank7_3(), Rank7_4())

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}
