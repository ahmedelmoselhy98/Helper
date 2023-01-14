package com.chefshub.app.presentation.main_video.profile

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.chefshub.app.R
import com.chefshub.app.databinding.FragmentProfileFragmentBinding
import com.chefshub.base.BaseFragment
import com.chefshub.data.entity.user.UserModel
import com.chefshub.utils.ext.loadImage
import com.chefshub.utils.ext.tint
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ProfileFragmentFragment : BaseFragment(R.layout.fragment_profile_fragment) {
    companion object {
        var userId: Int? = null
    }

    private var _binding: FragmentProfileFragmentBinding? = null
    private val binding get() = _binding!!
    private val profileVideosAdapter = ProfileVideosAdapter()

    private val userViewModel: ProfileViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProfileFragmentBinding.bind(view)

        userViewModel.getUserProfile(userId == null, userId)
        binding.apply {
            recyclerViewVideos.adapter = profileVideosAdapter
        }
        observeFlow()
        setupTabs()
        setupActions()
    }

    private fun setupActions() {
        binding.toolbar.ivShare.setOnClickListener { shareProfile(userModel?.id) }
        binding.toolbar.ivBack.setOnClickListener { findNavController().navigateUp() }
        binding.btnFollow.setOnClickListener { toggleFollow() }
    }

    private fun shareProfile(id: Int?) {
///
    }

    private fun toggleFollow() {
        userModel?.id?.let { userViewModel.toggleFollow(it) }
    }

    private fun setupTabs() {
        setupTabsIcon()
        setupTabSelectoinListener()
        selectTab(binding.tabLayout.getTabAt(0)!!, true)
    }

    private fun setupTabSelectoinListener() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                selectTab(binding.tabLayout.getTabAt(0)!!, tab?.position == 0)
                selectTab(binding.tabLayout.getTabAt(1)!!, tab?.position == 1)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
    }

    private fun setupTabsIcon() {
        binding.tabLayout.apply {
            addTab(setCustomTab(R.drawable.ic_categories, getString(R.string.gridView)))
            addTab(setCustomTab(R.drawable.ic_baseline_menu_24, getString(R.string.listView)))
        }
    }

    private fun observeFlow() {
        handleSharedFlow(userViewModel.userFlow, onSuccess = {
            if (it is UserModel) {
                setUser(it)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        loadUserVideos()
    }

    private fun loadUserVideos() {
        lifecycleScope.launchWhenStarted {
            if (userId == null) return@launchWhenStarted
            userViewModel.videos(userId!!).collectLatest {
                profileVideosAdapter.submitData(it)
            }
        }
    }

    private var userModel: UserModel? = null
    private fun setUser(it: UserModel) {
        this.userModel = it
        binding.apply {
            ivUserCover.loadImage(it.avatarPath)
            userImage.loadImage(it.avatarPath)
            userName.text = it.name
            userBio.text = it.bio
            tvCountPosts.text = it.postsCount.toString()
            tvCountFollowers.text = it.followersCount.toString()

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    private fun selectTab(tab: TabLayout.Tab, select: Boolean) {
        tab.customView?.apply {
            findViewById<TextView>(R.id.tvTabItem).setTextColor(
                ContextCompat.getColor(
                    context, if (select) R.color.blue else R.color.gray
                )
            )
            findViewById<ImageView>(R.id.ivTabIcon).tint(
                if (select) R.color.blue else R.color.gray
            )
        }
    }

    private fun setCustomTab(icon: Int, title: String): TabLayout.Tab {
        val tab = binding.tabLayout.newTab()
        tab.setCustomView(R.layout.custom_tab)
        tab.customView?.apply {
            findViewById<TextView>(R.id.tvTabItem).text = title
            findViewById<ImageView>(R.id.ivTabIcon).setImageResource(icon)
        }
        return tab
    }

}


