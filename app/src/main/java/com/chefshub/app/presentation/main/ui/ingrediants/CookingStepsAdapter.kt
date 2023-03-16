package com.chefshub.app.presentation.main.ui.ingrediants

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.chefshub.app.R
import com.chefshub.app.databinding.ItemCookingStepsBinding
import com.chefshub.app.databinding.ItemIngrdientsBinding
import com.chefshub.data.entity.tutorial.TutorialModel
import com.chefshub.utils.ext.loadImage

class CookingStepsAdapter : RecyclerView.Adapter<CookingStepsAdapter.ViewHolder>() {

    private val videoList = ArrayList<TutorialModel>()

    var step = 1

    inner class ViewHolder(val item: ItemCookingStepsBinding) : RecyclerView.ViewHolder(item.root) {

        fun bind() {
            item.ivMeal.loadImage(videoList[bindingAdapterPosition].url)
            item.caption.text = videoList[bindingAdapterPosition].caption
            item.step.setText("step ${step}")
            step++
        }

        init {
            item.root.setOnClickListener {
                Navigation.findNavController(it).navigate(
                    R.id.FragmentVideoIngredients,
                    bundleOf("url" to videoList.get(bindingAdapterPosition).url.toString())
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemCookingStepsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount() = videoList.size

    fun setAll(it: ArrayList<TutorialModel>) {
        step = 1
        this.videoList.apply {
            clear()
            addAll(it)
        }
        notifyDataSetChanged()
    }

}