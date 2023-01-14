package com.chefshub.app.presentation.main_video.video

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.viewpager2.widget.ViewPager2
import com.chefshub.app.R
import com.chefshub.app.databinding.FragmentVidesPlayerBinding
import com.chefshub.app.presentation.login.LoginActivity
import com.chefshub.app.presentation.main.ui.vedios.TutorialViewModel
import com.chefshub.app.presentation.main_video.comments.CommentsFragment
import com.chefshub.app.presentation.main_video.profile.ProfileFragmentFragment
import com.chefshub.base.BaseActivity
import com.chefshub.base.BaseFragment
import com.chefshub.data.cache.PreferencesGateway
import com.chefshub.data.entity.user.UserModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "VediosPlayerFragment"

@AndroidEntryPoint
class VediosPlayerFragment : BaseFragment(R.layout.fragment_vides_player) {

    private lateinit var binding: FragmentVidesPlayerBinding

    private val viewModel: TutorialViewModel by activityViewModels()

    val videosAdapter by lazy {
        VideosAdapter(
            requireActivity(),
            onSearchHasTag = { searchFor(it) },
            onOpenProfile = { openProfile(it) },
            onComments = { openComments(it.first, it.second ) },
            onToggleFollow = { toggleFollow(it) },
            onFavorite = { addFavorite(it) },
            myImage = getUserImage(),
            viewModel = viewModel)
    }

    private fun toggleFollow(it: Int) {
        viewModel.toggleFollow(it)
    }

    private fun addFavorite(it: Int) {
        viewModel.addFavorite(it)
    }

    @Inject
    lateinit var preferencesGateway: PreferencesGateway
    private fun getUserImage() = preferencesGateway.load(PrefKeys.USER, UserModel())?.avatarPath

    private fun openProfile(it: Int) {
        findNavController().navigate(R.id.profileFragmentFragment, bundleOf("value" to it))
    }

    private fun searchFor(it: String) {
        findNavController().navigate(R.id.navigation_home, bundleOf("value" to it))
    }

    private fun openComments(id: Int ,num:Int) {
        findNavController().navigate(
            R.id.commentsFragment,
            bundleOf(CommentsFragment.POST_ID to id ,CommentsFragment.NUM_COMMENTS to num )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVidesPlayerBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        binding.apply {
            swipeRefresh.setOnRefreshListener {
                videosAdapter.refresh()
            }

            observeFlow()



            viewPager2.adapter = videosAdapter
            viewPager2.setHasTransientState(false)
            viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)

//                    videosAdapter.playVideoAt(position)
                    ProfileFragmentFragment.userId=videosAdapter.getItemAt(position)

//                    viewModel.singleVideo(1)

//                    ProfileFragmentFragment.userId=videosAdapter.getItemAt(position)

                    if (viewModel.isLogin(requireContext()).not()) {
                        if (position >= 4) {
                            if (activity is BaseActivity) {
                                (activity as BaseActivity).apply {
                                    startActivity(LoginActivity::class.java)
                                    finish()
                                }
                            }
                        }
                    }
                }

                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                    super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                    Log.e(TAG, "onPageScrolled: $position , $positionOffset , $positionOffsetPixels")
                }

                override fun onPageScrollStateChanged(state: Int) {
                    super.onPageScrollStateChanged(state)
                    Log.e(TAG, "onPageScrollStateChanged: $state")
                    if (state == 1)
                        videosAdapter.dim()
                    else videosAdapter.reset()
                }
            })
        }
        setAdapterStates()
    }

    private fun setAdapterStates() {
        lifecycleScope.launch {
            videosAdapter.loadStateFlow.collectLatest { loadStates ->
                when (loadStates.refresh) {
                    is LoadState.NotLoading -> {
                        binding.swipeRefresh.isRefreshing = false
//                        setEmpty(notificationsAdapter.itemCount == 0)
                    }
                    is LoadState.Loading -> {
//                        setEmpty(false)
                        binding.swipeRefresh.isRefreshing = true

                    }
                    is LoadState.Error -> {
//                        setEmpty(false)
                        binding.swipeRefresh.isRefreshing = false

                        handleErrorGeneral((loadStates.refresh as LoadState.Error).error)
                    }
                }
            }
        }
    }

    private fun observeFlow() {
        handleSharedFlow(viewModel.toggleFollow, onSuccess = {

        })

        handleSharedFlow(viewModel.addFavorite, onSuccess = {

        })
        handleSharedFlow(viewModel.singleVideo, onSuccess = {
            Log.e("vvvvvvv"," tt "+it )
//            getSingleVideo(it as TutorialVideos)
        })


        lifecycleScope.launchWhenStarted {
            viewModel.list.collectLatest {
                Log.e("ttttt"," list "+it)
                videosAdapter.submitData(it)
            }
        }

//        lifecycleScope.launchWhenStarted {
//            viewModel.singleVideo.collectLatest {
//                Log.e("ttttt"," singleVideo "+it)
//
//            }
//        }
//        handleSharedFlow(viewModel.videoFlow, onSuccess = {
//            videosAdapter.submitData(it)
//        }, onShowProgress = {
//            binding.swipeRefresh.isRefreshing = true
//        }, onError = {
//            binding.swipeRefresh.isRefreshing = false
//        })
    }

    override fun onPause() {
        super.onPause()
        videosAdapter.playVideoAt(-1)
    }


}