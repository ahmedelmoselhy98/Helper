package com.chefshub.app.presentation.main.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chefshub.app.R
import com.chefshub.app.databinding.ItemImgBinding
import com.chefshub.app.databinding.ItemPickYourMealBinding
import com.chefshub.app.databinding.ItemVideosMainBinding
import com.chefshub.data.entity.tutorial.TutorialModel
import com.chefshub.utils.ext.loadImage
import java.util.ArrayList

class MostViewAdapter:
    RecyclerView.Adapter<MostViewAdapter.ViewHolder>() {
    inner class ViewHolder(val item: ItemImgBinding) : RecyclerView.ViewHolder(item.root) {
        fun bind() {
            item.apply {
                ivImage.setImageResource(R.drawable.mask)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemImgBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind()

    override fun getItemCount() = 15
    private val mealList = ArrayList<String>()
    fun setAll(it: List<String>) {
        this.mealList.apply {
            clear()
            addAll(it)
            notifyDataSetChanged()
        }
    }
}