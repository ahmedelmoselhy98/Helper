package com.chefshub.app.presentation.main_video.my_profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.chefshub.app.R
import com.chefshub.app.databinding.FragmentMyProfileBinding
import com.chefshub.app.presentation.login.LoginActivity
import com.chefshub.app.presentation.login.LoginViewModel
import com.chefshub.app.presentation.main.MainViewModel
import com.chefshub.app.presentation.select_pref.PrefActivity
import com.chefshub.base.BaseActivity
import com.chefshub.base.BaseFragment
import com.chefshub.data.cache.PreferencesGateway
import com.chefshub.utils.ext.loadImage
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import createDynamicLink
import dagger.hilt.android.AndroidEntryPoint
import shareDeepLink

private const val TAG = "MyProfileFragment"
@AndroidEntryPoint
class MyProfileFragment : BaseFragment(R.layout.fragment_my_profile) {

    private var _binding: FragmentMyProfileBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel: MainViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMyProfileBinding.bind(view)
        setupActions()
    }

    private fun setupActions() {
        binding.toolbar.ivShare.isVisible = false
        binding.toolbar.ivBack.setOnClickListener { findNavController().navigateUp() }
        binding.toolbar.tvTitle.setText(R.string.profile)


        binding.userImg.loadImage(mainViewModel.getUserProfile())
        binding.userName.text = mainViewModel.getUser()?.name
        binding.userNickName.text = mainViewModel.getUser()?.name
        binding.favouriteCuisine.setOnClickListener {
            startActivity(Intent(requireActivity(),PrefActivity::class.java))
        }

        binding.logout.setOnClickListener {
            val googleSignInClient = GoogleSignIn.getClient(requireContext(), GoogleSignInOptions.DEFAULT_SIGN_IN)
            googleSignInClient.signOut()
            PreferencesGateway(requireContext()).clearAll()
            startActivity(Intent(requireContext() , LoginActivity::class.java))
            requireActivity().finishAffinity()
        }

        binding.btnEdit.setOnClickListener {
            findNavController().navigate(R.id.EditMyProfile)
        }
        binding.savedMeal.setOnClickListener {
            findNavController().navigate(R.id.MySavedTutorialFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}


