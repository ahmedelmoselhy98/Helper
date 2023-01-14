package com.chefshub.app.presentation.main.ui.ingrediants

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.chefshub.app.R
import com.chefshub.app.databinding.ItemChefsBinding
import com.chefshub.app.databinding.ItemIngrdientsBinding

class IngredientsAdapter : RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {
    inner class ViewHolder(val item: ItemIngrdientsBinding) : RecyclerView.ViewHolder(item.root) {

        init {
            item.root.setOnClickListener {
                Navigation.findNavController(it).navigate(R.id.profileFragmentFragment)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemIngrdientsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }

    override fun getItemCount() = 10

}