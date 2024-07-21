package com.example.talkssogi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class fragmentPage6 : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_page6, container, false)

        val btnBack: ImageView = view.findViewById(R.id.imageView) //뒤로가기
        val searchbtn : ImageButton = view.findViewById(R.id.create) //검색
        val spinner: Spinner = view.findViewById(R.id.spinner)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView_wordclould) //이미지 넣을 리싸이클러뷰
        recyclerView.layoutManager = LinearLayoutManager(context)

        // 뒤로가기 이미지 리스너
        btnBack.setOnClickListener {
            // 프래그먼트 매니저를 통해 뒤로 가기 동작
            requireActivity().supportFragmentManager.popBackStack()
        }

        // 검색 버튼 클릭 리스너 설정
        searchbtn.setOnClickListener {

            // 서버에 이미지 요청하기
            // Retrofit을 사용한 서버 요청
            //mainViewModel.fetchImages(selectedSearchItem, selectedResultsItem))
            // 서버에서 이미지를 url로 받아서 어댑터에 넣기(?)
//            val imageUrl = "android.resource://${requireContext().packageName}/${R.drawable.test_img_page9}" // 서버 구축 전 테스트 사진
//            val testImageResponse = ImageResponse(imageUrl)
//            // RecyclerViewAdapter_page9 초기화
//            val itemList = listOf(testImageResponse)
//            val adapter = RecyclerViewAdapter_page9(itemList)
//            recyclerView.adapter = adapter

            // 일단 테스트용 코드
            val imageUrl = "https://example.com/test_img_page6.jpg" // 실제 URL로 변경해야 함
            val testImageResponse = ImageURL(imageUrl)
            val itemList = listOf(testImageResponse)

            // 리싸이클러뷰 어댑터 초기화 (모든 프래그먼트에서 같은 어댑터를 사용)
            val recyclerViewAdapter = RecyclerViewAdapter(itemList)
            recyclerView.adapter = recyclerViewAdapter

        }

        // 스피너 아이템 설정
        // 뷰모델에서 대화 참여자 이름 가져와서 저장하기
        val names = arrayOf("전체", "친구 1", "친구 2", "친구 3", "친구 4", "친구 5")
        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, names)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = spinnerAdapter


        // 초기 버튼 클릭 전에 보여질 리싸이클러뷰 아이템 설정 (테스트용 코드)
        val imageUrlForRecyclerView = "https://example.com/phone.jpg" // 실제 URL로 변경해야 함
        val testImageResponseForRecyclerView = ImageURL(imageUrlForRecyclerView)
        val itemListForRecyclerView = listOf(testImageResponseForRecyclerView)
        val recyclerViewAdapter = RecyclerViewAdapter(itemListForRecyclerView)
        recyclerView.adapter = recyclerViewAdapter

        // 인플레이트된 뷰를 반환합니다.
        return view
    }
}