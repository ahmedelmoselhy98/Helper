package com.chefshub.app.presentation.main.ui.checkout

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.chefshub.app.R
import com.chefshub.app.databinding.FragmentCheckoutBinding
import com.chefshub.base.BaseFragment

class CheckoutFragment : BaseFragment(R.layout.fragment_checkout) {

    private val viewModel: CheckoutViewModel by viewModels()

    private var _binding: FragmentCheckoutBinding? = null
    private val binding get() = _binding!!


    override fun onAttach(context: Context) {
        super.onAttach(context)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCheckoutBinding.bind(view)

        setAction()
    }

    private fun setAction() {
        with(binding) {
            btnPayment.setOnClickListener { findNavController().navigate(R.id.dialogSuccessOrder) }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}