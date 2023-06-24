package com.chefshub.app.presentation.main.ui.home.searchByMeal

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chefshub.app.databinding.ItemVideosMainBinding
import com.chefshub.data.entity.search.MealResponse
import com.chefshub.data.entity.tutorial.TutorialModel
import com.chefshub.data.entity.tutorial.TutorialVideos
import com.chefshub.utils.ext.loadImage
import java.util.ArrayList

class MealTypeAdapter constructor(private val onVideoClicked: () -> Unit) :
    RecyclerView.Adapter<MealTypeAdapter.ViewHolder>() {
    inner class ViewHolder(val item: ItemVideosMainBinding) : RecyclerView.ViewHolder(item.root) {
        fun bind(data: MealResponse) {
            item.apply {
                videoImg.loadImage(data.logo_path)
                title.setText(data.title)
                cfName.setText(data.chef?.name)
                like.setText(data.main_video?.favouritesCount.toString())
                view.setText(data.main_video?.views.toString())
            }
        }

        init {
            item.root.setOnClickListener {
                if (bindingAdapterPosition == -1) return@setOnClickListener

                it.context.apply {
                    onVideoClicked.invoke()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemVideosMainBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(videosList.get(position))

    override fun getItemCount() = videosList.size
    private val videosList = ArrayList<MealResponse>()
    fun setAll(it: ArrayList<MealResponse>) {
        this.videosList.apply {
            clear()
            addAll(it)
            notifyDataSetChanged()
        }
    }
}