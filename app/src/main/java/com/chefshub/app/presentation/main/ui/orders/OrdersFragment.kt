package com.chefshub.app.presentation.main.ui.orders

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.chefshub.app.R
import com.chefshub.app.databinding.FragmentOrdersBinding
import com.chefshub.base.BaseFragment

class OrdersFragment : BaseFragment(R.layout.fragment_orders) {
    private var _binding: FragmentOrdersBinding? = null
    private val binding get() = _binding!!

    private val cartAdapter by lazy { OrdersAdapter() }
    private val viewModel: OrdersViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentOrdersBinding.bind(view)
        setAction()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        binding.recyclerViewOrders.apply {
            setHasFixedSize(true)
            adapter = cartAdapter
        }
    }

    private fun setAction() {
        with(binding) {
            btnCheckout.setOnClickListener { findNavController().navigate(R.id.checkoutFragment) }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}