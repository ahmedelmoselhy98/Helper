package com.chefshub.app.presentation.main.ui.ingrediants

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.chefshub.app.R
import com.chefshub.app.databinding.FragmentIngedientsBinding

class IngedientsFragment : Fragment(R.layout.fragment_ingedients) {
    private var _binding: FragmentIngedientsBinding? = null

    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentIngedientsBinding.bind(view)

        binding.recyclerView.apply {
            setHasFixedSize(true)
            adapter = IngredientsAdapter()
        }

        setAction()
    }

    private fun setAction() {
        with(binding) {
            ivCart.setOnClickListener { findNavController().navigate(R.id.ordersFragment) }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}