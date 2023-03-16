package com.chefshub.app.presentation.main_video.profile

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chefshub.app.databinding.ItemVideosBinding
import com.chefshub.app.presentation.main_video.video.DIFF_UTILS
import com.chefshub.data.entity.tutorial.TutorialModel
import com.chefshub.utils.ext.loadImage

class ProfileVideosAdapter : PagingDataAdapter<TutorialModel, ProfileVideosAdapter.ViewHolder>(
    DIFF_UTILS
) {
    companion object {
        var visible = true
    }

    inner class ViewHolder(val item: ItemVideosBinding) : RecyclerView.ViewHolder(item.root) {
        init {

            Log.e("ProfileVideosAdapter", "ProfileVideosAdapter ")
        }


        fun bind(position: Int): Unit {


            item.apply {
                val item = getItem(position)
                Log.e("ProfileVideosAdapter", "ProfileVideosAdapter "+item)
                tvMealName.text = item?.title
                tvDescription.text =
                    "2h" + "  " + item?.videosCount + " Views"
//                     item?.lengthInMinutes + "m  " + item?.videosCount + " Views"
                tvMealIcon.loadImage(item?.logoPath)
            }
        }

        fun bind2(position: Int): Unit {

            item.apply {
                val item = getItem(position)

                tvMealName.isVisible =false
                tvDescription.isVisible =false
                tvMealIcon.loadImage(item?.logoPath)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemVideosBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        if (visible)
            holder.bind(position)
//        else holder.bind2(position)
    }

}
