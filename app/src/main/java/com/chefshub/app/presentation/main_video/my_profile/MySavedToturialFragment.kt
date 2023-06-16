package com.chefshub.app.presentation.main_video.my_profile

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.chefshub.app.R
import com.chefshub.app.databinding.FragmentSavedToturialBinding
import com.chefshub.app.presentation.login.LoginViewModel
import com.chefshub.app.presentation.main_video.my_profile.adapter.MySavedTutorialAdapter
import com.chefshub.base.BaseFragment
import com.chefshub.data.entity.bookmarked.VideoModel
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "MySavedToturialFragment"
@AndroidEntryPoint
class MySavedToturialFragment : BaseFragment(R.layout.fragment_saved_toturial) {

    private var _binding: FragmentSavedToturialBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginViewModel by viewModels()
    private val videosAdapter = MySavedTutorialAdapter()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSavedToturialBinding.bind(view)

        binding.recyclerViewTutorial.adapter = videosAdapter

        viewModel.getTutorial()
        setupObserver()
        setupActions()
    }

    private fun setupActions() {
        binding.toolbar.ivShare.isVisible = false
        binding.toolbar.ivBack.setOnClickListener { findNavController().navigateUp() }
        binding.toolbar.tvTitle.setText(R.string.savedVideo)

    }

    private fun setupObserver() {
        handleSharedFlow(viewModel.getTutorialFlow, onSuccess = {
            if (it is ArrayList<*>) {
                videosAdapter.setAll(it as ArrayList<VideoModel>)
            }
        })}

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}


