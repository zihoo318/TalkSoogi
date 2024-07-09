package com.example.talkssogi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.yourpackage.ui.main.ViewPagerAdapter
//import me.relex.circleindicator.CircleIndicator3

class fragmentPage7 : Fragment() {

    private lateinit var viewPager2: ViewPager2
    //private lateinit var indicator: CircleIndicator3

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_page7, container, false)

        viewPager2 = view.findViewById(R.id.view_pager)
        //indicator = view.findViewById(R.id.ranking_indicator)  // CircleIndicator3의 id로 수정

        viewPager2.adapter = ViewPagerAdapter(requireActivity())

        // CircleIndicator와 ViewPager2 연결
        //indicator.setViewPager(viewPager2)

        view.findViewById<ImageButton>(R.id.button_previous).setOnClickListener {
            val previousItem = if (viewPager2.currentItem - 1 >= 0) viewPager2.currentItem - 1 else 0
            viewPager2.setCurrentItem(previousItem, true)
        }

        view.findViewById<ImageButton>(R.id.button_next).setOnClickListener {
            val nextItem = if (viewPager2.currentItem + 1 < viewPager2.adapter?.itemCount ?: 0) viewPager2.currentItem + 1 else viewPager2.adapter?.itemCount ?: 0 - 1
            viewPager2.setCurrentItem(nextItem, true)
        }

        return view
    }
}
