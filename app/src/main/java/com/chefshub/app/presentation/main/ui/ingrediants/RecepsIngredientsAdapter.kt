package com.chefshub.app.presentation.main.ui.ingrediants

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.chefshub.app.R
import com.chefshub.app.databinding.ItemCookingStepsBinding
import com.chefshub.app.databinding.ItemIngrdientsBinding
import com.chefshub.app.databinding.ItemIngredientsBinding
import com.chefshub.data.entity.ingredients.IngredientsModel
import com.chefshub.data.entity.tutorial.TutorialModel
import com.chefshub.utils.ext.loadImage

class RecepsIngredientsAdapter : RecyclerView.Adapter<RecepsIngredientsAdapter.ViewHolder>() {

    private val videoList = ArrayList<IngredientsModel>()

    var step = 1

    inner class ViewHolder(val item: ItemIngredientsBinding) : RecyclerView.ViewHolder(item.root) {

        fun bind() {

            item.amount.text = ((videoList[bindingAdapterPosition].amount)?.times(IngedientsFragment.totalAmount!!)).toString()
            item.unit.text = videoList[bindingAdapterPosition].unit.toString()
            item.name.text = videoList[bindingAdapterPosition].name.toString()
//            item.ivMeal.loadImage(videoList[bindingAdapterPosition].url)
//            item.step.setText("step ${step}")
//            step++
        }

        init {
//            item.root.setOnClickListener {
//                Navigation.findNavController(it).navigate(
//                    R.id.FragmentVideoIngredients,
//                    bundleOf("url" to videoList.get(bindingAdapterPosition).url.toString())
//                )
//            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemIngredientsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount() = videoList.size
//        videoList.size

    fun setAll(it: ArrayList<IngredientsModel>) {
        step = 1
        this.videoList.apply {
            clear()
            addAll(it)
        }
        notifyDataSetChanged()
    }

}