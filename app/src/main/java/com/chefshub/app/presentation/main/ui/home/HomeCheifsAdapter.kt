package com.chefshub.app.presentation.main.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.chefshub.app.R
import com.chefshub.app.databinding.ItemChefsBinding
import com.chefshub.app.presentation.main_video.profile.ProfileFragmentFragment
import com.chefshub.data.entity.user.UserModel
import com.chefshub.utils.ext.loadImage

class HomeCheifsAdapter : RecyclerView.Adapter<HomeCheifsAdapter.ViewHolder>() {
    private val chefsList = ArrayList<UserModel>()

    inner class ViewHolder(val item: ItemChefsBinding) : RecyclerView.ViewHolder(item.root) {
        fun bind(data: UserModel) {
            item.apply {
                ivUserImage.loadImage(data.avatarPath)
                tvUserName.text = data.name
            }
        }

        init {
            item.root.setOnClickListener {
                if (bindingAdapterPosition == -1) return@setOnClickListener
                ProfileFragmentFragment.userId = chefsList[bindingAdapterPosition].id
                Navigation.findNavController(it).navigate(R.id.profileFragmentFragment)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemChefsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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