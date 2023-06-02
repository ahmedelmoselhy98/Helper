package com.chefshub.app.presentation.main.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.chefshub.app.R
import com.chefshub.app.databinding.ItemImgBinding
import com.chefshub.app.presentation.main_video.profile.ProfileFragmentFragment
import com.chefshub.data.entity.search.MostViewResponse
import com.chefshub.utils.ext.loadImage
import java.util.ArrayList

class MostViewAdapter:
    RecyclerView.Adapter<MostViewAdapter.ViewHolder>() {
    inner class ViewHolder(val item: ItemImgBinding) : RecyclerView.ViewHolder(item.root) {
        fun bind(position: Int) {
            item.apply {
                ivImage.setImageResource(R.drawable.mask)
                ivImage.loadImage(chefList[position].avatarPath)
            }
        }
        init {
            item.root.setOnClickListener {
                if (bindingAdapterPosition == -1) return@setOnClickListener
                ProfileFragmentFragment.userId = chefList[bindingAdapterPosition].id
                ProfileFragmentFragment.userimage = chefList[bindingAdapterPosition].avatarPath
                ProfileFragmentFragment.name = chefList[bindingAdapterPosition].name
                Navigation.findNavController(it).navigate(R.id.profileFragmentFragment)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemImgBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(position)

    override fun getItemCount() = chefList.size
    private val chefList = ArrayList<MostViewResponse>()
    fun setAll(it: List<MostViewResponse>) {
        this.chefList.apply {
            clear()
            addAll(it)
            notifyDataSetChanged()
        }
    }
}