package com.chefshub.app.presentation.main.ui.orders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.chefshub.app.R
import com.chefshub.app.databinding.ItemChefsBinding
import com.chefshub.app.databinding.ItemIngrdientsBinding
import com.chefshub.app.databinding.ItemMyCartBinding

class OrdersAdapter : RecyclerView.Adapter<OrdersAdapter.ViewHolder>() {
    inner class ViewHolder(val item: ItemMyCartBinding) : RecyclerView.ViewHolder(item.root) {

        init {
            item.root.setOnClickListener {
                Navigation.findNavController(it).navigate(R.id.profileFragmentFragment)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemMyCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }

    override fun getItemCount() = 10

}