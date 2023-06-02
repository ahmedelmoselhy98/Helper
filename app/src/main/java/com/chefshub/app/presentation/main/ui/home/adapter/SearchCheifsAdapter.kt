package com.chefshub.app.presentation.main.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.chefshub.app.R
import com.chefshub.app.databinding.ItemVideosMainBinding
import com.chefshub.app.presentation.main_video.profile.ProfileFragmentFragment
import com.chefshub.data.entity.user.UserModel
import com.chefshub.utils.ext.loadImage

class SearchCheifsAdapter : RecyclerView.Adapter<SearchCheifsAdapter.ViewHolder>() {
    private val chefsList = ArrayList<UserModel>()

    inner class ViewHolder(val item: ItemVideosMainBinding) : RecyclerView.ViewHolder(item.root) {
        fun bind(data: UserModel) {
            item.apply {
                videoImg.loadImage(data.avatarPath)
                cfName.isVisible=false
                title.setText(data.name)
                like.setText(data.regionalCuisinesCount.toString())
                view.setText(data.regionalCuisinesCount.toString())
            }
        }

        init {
            item.root.setOnClickListener {
                if (bindingAdapterPosition == -1) return@setOnClickListener

                ProfileFragmentFragment.userId = chefsList[bindingAdapterPosition].id
                ProfileFragmentFragment.userimage = chefsList[bindingAdapterPosition].avatarPath
                ProfileFragmentFragment.name = chefsList[bindingAdapterPosition].name
                Navigation.findNavController(it).navigate(R.id.profileFragmentFragment)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemVideosMainBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(chefsList.get(position))

    override fun getItemCount() = chefsList.size
    fun setAll(it: ArrayList<UserModel>) {
        this.chefsList.apply {
            clear()
            addAll(it)
        }
        notifyDataSetChanged()
    }

}