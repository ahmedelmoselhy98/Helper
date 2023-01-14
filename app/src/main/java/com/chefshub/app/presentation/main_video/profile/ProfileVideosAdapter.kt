package com.chefshub.app.presentation.main_video.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chefshub.app.databinding.ItemVideosBinding
import com.chefshub.app.presentation.main_video.video.DIFF_UTILS
import com.chefshub.data.entity.tutorial.TutorialModel
import com.chefshub.utils.ext.loadImage

class ProfileVideosAdapter : PagingDataAdapter<TutorialModel, ProfileVideosAdapter.ViewHolder>(
    DIFF_UTILS
) {
    inner class ViewHolder(val item: ItemVideosBinding) : RecyclerView.ViewHolder(item.root) {

        fun bind(position: Int): Unit {
            item.apply {
                val item = getItem(position)
                tvDescription.text = item?.title
                tvMealName.text = item?.title
                tvMealIcon.loadImage(item?.chef?.avatarPath)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemVideosBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(position)


}