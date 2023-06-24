package com.chefshub.app.presentation.main_video.my_profile.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.chefshub.app.R
import com.chefshub.app.databinding.ItemVideosMainBinding
import com.chefshub.app.presentation.main.ui.ingrediants.IngedientsFragment
import com.chefshub.app.presentation.main.ui.singleVideo.SingleVideoLoadFragment
import com.chefshub.data.entity.bookmarked.VideoModel
import com.chefshub.utils.ext.loadImage

class MySavedTutorialAdapter  :
    RecyclerView.Adapter<MySavedTutorialAdapter.ViewHolder>() {
    inner class ViewHolder(val item: ItemVideosMainBinding) : RecyclerView.ViewHolder(item.root) {

        init {
            item.root.setOnClickListener {
                if (bindingAdapterPosition == -1) return@setOnClickListener

                SingleVideoLoadFragment.videoId = videosList[bindingAdapterPosition].id
                IngedientsFragment.tutorial_id =  videosList[bindingAdapterPosition]?.tutorial_id
                IngedientsFragment.ingredients_id = videosList[bindingAdapterPosition]?.id
                IngedientsFragment.background = videosList[bindingAdapterPosition]?.screenshot_url

                Navigation.findNavController(it).navigate(R.id.singleVideoFragment)

            }
        }

        fun bind(data: VideoModel) {
            item.apply {
                videoImg.loadImage(data.screenshot_url)
                title.setText(data.title)
                cfName.setText(data.caption)
                like.isVisible=false
                view.isVisible=false
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemVideosMainBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(videosList.get(position))

    override fun getItemCount() = videosList.size
    private val videosList = ArrayList<VideoModel>()
    fun setAll(it: ArrayList<VideoModel>) {
        this.videosList.apply {
            clear()
            addAll(it)
            notifyDataSetChanged()
        }
    }
}