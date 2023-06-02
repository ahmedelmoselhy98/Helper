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

//class ProfileVideosAdapter : PagingDataAdapter<TutorialModel, ProfileVideosAdapter.ViewHolder>(
//    DIFF_UTILS
//) {
class ProfileVideosAdapter :  RecyclerView.Adapter<ProfileVideosAdapter.ViewHolder>() {

//    companion object {
//        var visible = true
//    }
    private val videoList = ArrayList<TutorialModel>()

    inner class ViewHolder(val item: ItemVideosBinding) : RecyclerView.ViewHolder(item.root) {

        fun bind(position: Int): Unit {

            item.apply {
//                val item = getItem(position)
                Log.e("ProfileVideosAdapter", "ProfileVideosAdapter "+item)
                tvMealName.text = videoList[position].title
                tvDescription.text =
                     "  ${videoList[position].videosCount?:0 } " + " Views"
//                     item?.lengthInMinutes + "m  " + item?.videosCount + " Views"
                tvMealIcon.loadImage(videoList[position].screenshot_url)
            }
        }

        fun bind2(position: Int): Unit {

            item.apply {
//                val item = getItem(position)

                tvMealName.isVisible =false
                tvDescription.isVisible =false
//                tvMealIcon.loadImage(item?.logoPath)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemVideosBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.e("tester", "onBindViewHolder: position")
//        if (visible)
            holder.bind(position)
//        else holder.bind2(position)
    }

    override fun getItemCount() = videoList.size

    fun setAll(it: ArrayList<TutorialModel>) {
//        step = 1
        this.videoList.apply {
            clear()
            addAll(it)
        }
        notifyDataSetChanged()
    }



}
