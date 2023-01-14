package com.chefshub.app.presentation.main_video.comments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chefshub.data.entity.comments.CommentsModel

class CommentsAdapter : RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder>() {

    private val dataList = ArrayList<CommentsModel>()

    inner class CommentsViewHolder(private val item: com.chefshub.app.databinding.ItemCommentsBinding) :
        RecyclerView.ViewHolder(item.root) {
        fun bind() {

         item.tvComment.text = dataList.get(bindingAdapterPosition).body
         item.tvUserName.text =dataList.get(bindingAdapterPosition).creator.name
         item.tvFav.text ="0"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CommentsViewHolder(
        com.chefshub.app.databinding.ItemCommentsBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun onBindViewHolder(holder: CommentsViewHolder, position: Int) = holder.bind()

    override fun getItemCount() = dataList.size
    fun setAll(arrayList: java.util.ArrayList<CommentsModel>) {
        this.dataList.apply {
            clear()
            addAll(arrayList)
            notifyDataSetChanged()
        }
    }
}
