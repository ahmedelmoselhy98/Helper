package com.chefshub.app.presentation.main.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chefshub.app.databinding.ItemPickYourMealBinding
import com.chefshub.app.databinding.ItemVideosMainBinding
import com.chefshub.data.entity.tutorial.TutorialModel
import com.chefshub.utils.ext.loadImage
import java.util.ArrayList

class MealAdapter:
    RecyclerView.Adapter<MealAdapter.ViewHolder>() {
    inner class ViewHolder(val item: ItemPickYourMealBinding) : RecyclerView.ViewHolder(item.root) {
        fun bind(data: String) {
            item.apply {
                tvMeal.setText(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemPickYourMealBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(mealList.get(position))

    override fun getItemCount() = mealList.size
    private val mealList = ArrayList<String>()
    fun setAll(it: List<String>) {
        this.mealList.apply {
            clear()
            addAll(it)
            notifyDataSetChanged()
        }
    }
}