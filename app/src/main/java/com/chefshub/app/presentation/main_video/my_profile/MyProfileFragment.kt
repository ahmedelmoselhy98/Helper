package com.chefshub.app.presentation.main_video.my_profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.chefshub.app.R
import com.chefshub.app.databinding.FragmentMyProfileBinding
import com.chefshub.app.databinding.FragmentProfileFragmentBinding
import com.chefshub.app.presentation.main.ui.ingrediants.IngedientsFragment
import com.chefshub.app.presentation.main_video.profile.ChefVideosAdapter
import com.chefshub.app.presentation.main_video.profile.ProfileVideosAdapter
import com.chefshub.app.presentation.main_video.profile.ProfileViewModel
import com.chefshub.base.BaseActivity
import com.chefshub.base.BaseFragment
import com.chefshub.data.entity.tutorial.TutorialModel
import com.chefshub.data.entity.user.UserModel
import com.chefshub.utils.ext.loadImage
import com.chefshub.utils.ext.tint
import com.google.android.material.tabs.TabLayout
import createDynamicLink
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import shareDeepLink

@AndroidEntryPoint
class MyProfileFragment : BaseFragment(R.layout.fragment_my_profile) {
//    companion object {
//        var userId: Int? = null
//        var userimage: String ?= null
//        var name: String ?= null
//    }

//    private val viewModel: IngredientsViewModel by viewModels()
//    private val listvideosChef = IngredientsAdapter()

    var facebookUrl :String?=null
    var instegram :String?=null
    var youtupe :String?=null
    private val listvideosChef = ChefVideosAdapter()



    private var _binding: FragmentMyProfileBinding? = null
    private val binding get() = _binding!!
    private val profileVideosAdapter = ProfileVideosAdapter()
//    private val profileVideosAdapterGride = ProfileVideosAdapterGride()

    private val userViewModel: ProfileViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMyProfileBinding.bind(view)

//        if (!(userId == null || IngedientsFragment.tutorial_id!! ==null)){
//            userViewModel.getTutorialsVideosChef(userId!!)
////            viewModel.getIngredients(IngedientsFragment.tutorial_id!!)
//            userViewModel.getUserProfile(userId == null, userId)
//        }

        binding.apply {
//            binding.recyclerViewVideos.setLayoutManager(GridLayoutManager(requireContext(), 2))
            binding.recyclerViewVideos.setLayoutManager(GridLayoutManager( requireContext(),3))
            ProfileVideosAdapter.visible = false
            recyclerViewVideos.adapter = listvideosChef
        }

        observeFlow()
        setupTabs()
        setupActions()

    }

    private fun setupActions() {
        binding.toolbar.ivShare.setOnClickListener { shareProfile(userModel?.id) }
        binding.toolbar.ivBack.setOnClickListener { findNavController().navigate(R.id.videoFragment) }
        binding.btnFollow.setOnClickListener {
            if (binding.btnFollow.text.toString()=="follow"){
                binding.btnFollow.text ="following"
            }else{
                binding.btnFollow.text = "follow"
            }
            toggleFollow() }

        binding.btnFaceBook.setOnClickListener {
            try {
                if (!facebookUrl.isNullOrEmpty()){
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(facebookUrl))
                    startActivity(intent)
                }
            } catch (e: Exception) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("http://www.facebook.com/appetizerandroid")
                    )
                )
            }
        }
        binding.btnInstagram.setOnClickListener {
            try {
                if (!instegram.isNullOrEmpty()){
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(instegram))
                    startActivity(intent)
                }
            } catch (e: Exception) {

            }
        }
        binding.btnYoutube.setOnClickListener {
            try {
                if (!youtupe.isNullOrEmpty()){
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtupe))
                    startActivity(intent)
                }
            } catch (e: Exception) {

            }
        }

//        binding.userImage.loadImage(userimage)
//        binding.ivUserCover.loadImage(userimage)
//        binding.userName.text = name

    }

    private fun shareProfile(id: Int?) {

        createDynamicLink(requireActivity() as BaseActivity,id.toString() ){ dynamicLink->
            this.shareDeepLink(dynamicLink)
        }
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

                if (tab?.position == 0){
                    ProfileVideosAdapter.visible = false
//                    binding.recyclerViewVideos.adapter=profileVideosAdapter

                    binding.recyclerViewVideos.apply {
                        setHasFixedSize(true)
                        adapter = listvideosChef
                    }
                    binding.recyclerViewVideos.setLayoutManager(GridLayoutManager( requireContext(),3))
                }else{
                    ProfileVideosAdapter.visible = true

//                    binding.recyclerViewVideos.isVisible=true
                    binding.recyclerViewVideos.adapter = profileVideosAdapter
                    binding.recyclerViewVideos.setLayoutManager(LinearLayoutManager(requireContext()))

                }
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

        handleSharedFlow(userViewModel.VideosChefFlow, onSuccess = {
            Log.e("ingredientsFlow"," it "+it)
            listvideosChef.setAll(it as ArrayList<TutorialModel>)
        })

        handleSharedFlow(userViewModel.userFlow, onSuccess = {
            Log.e("myprofile"," itttttttttt "+it)
            if (it is UserModel && it !=null ) {
                setUser(it)
            }
        })

        handleSharedFlow(userViewModel.toggleFlow, onSuccess = {
            Log.e("fffffff"," msg "+it)
            Toast.makeText( requireContext()," "+it , Toast.LENGTH_SHORT).show()
        })
    }

    override fun onResume() {
        super.onResume()
//        Log.e("onResume","onResume "+ userId)
//        userViewModel.getTutorialsVideosChef(userId!!)
//
//        userViewModel.getUserProfile(userId == null, userId)
//        loadUserVideos()
//
//
//        binding.userImage.loadImage(userimage)
//        binding.ivUserCover.loadImage(userimage)
//        binding.userName.text = name
    }

    private fun loadUserVideos() {
        lifecycleScope.launchWhenStarted {
//            if (userId == null) return@launchWhenStarted
//            userViewModel.videos(userId!!).collectLatest {
//                Log.e("loadUserVideos","loadUserVideos "+ it)
////                profileVideosAdapterGride.submitData(it)
//                profileVideosAdapter.submitData(it)
//            }
        }
    }

    private var userModel: UserModel? = null
    private fun setUser(it: UserModel) {
        this.userModel = it
        binding.apply {
//            ivUserCover.loadImage(it.avatarPath)
//            userImage.loadImage(it.avatarPath)
//            userName.text = it.name
            if ( it !=null){
//                userBio.text = it.bio
                tvCountPosts.text = it.postsCount.toString()
                tvCountFollowers.text = it.followersCount.toString()
                tvCountCuisines.text = it.regionalCuisinesCount.toString()
                btnFollow.text  = if (it.is_following ==true) "following" else "follow"

                btnFaceBook.isVisible = !it.socialMedia?.facebook.isNullOrEmpty()
                btnInstagram.isVisible = if (!it.socialMedia?.instagram.isNullOrEmpty()) true else false
                btnYoutube.isVisible = if (!it.socialMedia?.youtube.isNullOrEmpty()) true else false

                facebookUrl = it.socialMedia?.facebook
                instegram = it.socialMedia?.instagram
                youtupe = it.socialMedia?.youtube
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    private fun selectTab(tab: TabLayout.Tab, select: Boolean) {
        tab.customView?.apply {
            findViewById<TextView>(R.id.tvTabItem).apply {
                setTextColor(
                    ContextCompat.getColor(
                        context, if (select) R.color.blue else R.color.gray
                    )
                )
            }

            findViewById<ImageView>(R.id.ivTabIcon).apply {
                tint(
                    if (select) R.color.blue else R.color.gray
                )
            }
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


