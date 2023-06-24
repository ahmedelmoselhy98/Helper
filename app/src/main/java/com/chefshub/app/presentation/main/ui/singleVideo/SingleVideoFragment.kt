package com.chefshub.app.presentation.main.ui.singleVideo

import android.os.Bundle
import android.view.View
import com.chefshub.app.R
import com.chefshub.app.databinding.FragmentSingleVideoPagerBinding
import com.chefshub.base.BaseFragment
import com.chefshub.utils.ext.reduceDragSensitivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SingleVideoFragment : BaseFragment(R.layout.fragment_single_video_pager) {
    private var _binding: FragmentSingleVideoPagerBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSingleVideoPagerBinding.bind(view)

        binding.viewPager2.apply {
            reduceDragSensitivity(2)
            adapter = SingleVideoPagerAdapter(this@SingleVideoFragment)
            setCurrentItem(1, false)
        }
    }
}