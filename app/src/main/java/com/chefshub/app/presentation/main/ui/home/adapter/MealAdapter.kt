package com.chefshub.app.presentation.main.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.chefshub.app.R
import com.chefshub.app.databinding.ItemPickYourMealBinding
import java.util.ArrayList

class MealAdapter:
    RecyclerView.Adapter<MealAdapter.ViewHolder>() {
    inner class ViewHolder(val item: ItemPickYourMealBinding) : RecyclerView.ViewHolder(item.root) {
        fun bind(data: String) {
            item.apply {
                tvMeal.setText(data)
            }
        }
        init {
            item.root.setOnClickListener {
                if (bindingAdapterPosition == -1) return@setOnClickListener

                findNavController(it).navigate(R.id.filterByMeal, bundleOf("type" to item.tvMeal.text.toString()))

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