package com.example.talkssogi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.squareup.picasso.Picasso

class Page9RecyclerViewAdapter(private val itemList: List<ImageURL>) :
    RecyclerView.Adapter<Page9RecyclerViewAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.item_image)

        fun bind(imageURL: ImageURL) {
            // Glide로 이미지 로드 및 캐시 비활성화
            Glide.with(imageView.context)
                .load(imageURL.imageUrl)
                //.placeholder(R.drawable.happy2) // 로딩 중 보여줄 이미지
                .error(R.drawable.error) // 에러 발생 시 보여줄 이미지
                .diskCacheStrategy(DiskCacheStrategy.NONE) // 디스크 캐시 사용 안 함
                .skipMemoryCache(true) // 메모리 캐시 사용 안 함
                .into(imageView)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imageUrl = itemList[position]
        holder.bind(imageUrl)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}