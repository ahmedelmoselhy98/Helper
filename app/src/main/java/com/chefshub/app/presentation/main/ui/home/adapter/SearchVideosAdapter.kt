package com.chefshub.app.presentation.main.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chefshub.app.databinding.ItemVideosMainBinding
import com.chefshub.data.entity.tutorial.TutorialModel
import com.chefshub.utils.ext.loadImage

class SearchVideosAdapter constructor(private val onVideoClicked: () -> Unit) :
    RecyclerView.Adapter<SearchVideosAdapter.ViewHolder>() {
    inner class ViewHolder(val item: ItemVideosMainBinding) : RecyclerView.ViewHolder(item.root) {
        fun bind(data: TutorialModel) {
            item.apply {
                videoImg.loadImage(data.imagePath)
                title.setText(data.title)
                cfName.setText(data.chef?.name)
                like.setText(data.favouritesCount.toString())
                view.setText(data.views.toString())
            }
        }

        init {
            item.root.setOnClickListener {
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
    private val videosList = ArrayList<TutorialModel>()
    fun setAll(it: ArrayList<TutorialModel>) {
        this.videosList.apply {
            clear()
            addAll(it)
            notifyDataSetChanged()
        }
    }
}