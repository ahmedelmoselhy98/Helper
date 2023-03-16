package com.chefshub.app.presentation.main_video.video

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.viewpager2.widget.ViewPager2
import com.chefshub.app.R
import com.chefshub.app.databinding.FragmentVidesPlayerBinding
import com.chefshub.app.databinding.ItemVedioPlayerBinding
import com.chefshub.app.presentation.App
import com.chefshub.app.presentation.login.LoginActivity
import com.chefshub.app.presentation.main.ui.ingrediants.IngedientsFragment
import com.chefshub.app.presentation.main.ui.vedios.TutorialViewModel
import com.chefshub.app.presentation.main_video.comments.CommentsFragment
import com.chefshub.app.presentation.main_video.profile.ProfileFragmentFragment
import com.chefshub.base.BaseActivity
import com.chefshub.base.BaseFragment
import com.chefshub.data.cache.PreferencesGateway
import com.chefshub.data.entity.tutorial.TutorialModel
import com.chefshub.data.entity.tutorial.TutorialVideos
import com.chefshub.data.entity.user.UserModel
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.HttpDataSource
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "VediosPlayerFragment"

@AndroidEntryPoint
class VediosPlayerFragment : BaseFragment(R.layout.fragment_vides_player) {

    private lateinit var binding: FragmentVidesPlayerBinding
    private val viewModel: TutorialViewModel by activityViewModels()

//    private lateinit var mHttpDataSourceFactory: HttpDataSource.Factory
//    private lateinit var mDefaultDataSourceFactory: DefaultDataSourceFactory
//    private lateinit var mCacheDataSourceFactory: DataSource.Factory
//    private lateinit var exoPlayer: SimpleExoPlayer
//    private val cache: SimpleCache = App.cache


    val videosAdapter by lazy {

//        var exoPlayer = ExoPlayer.Builder(requireActivity()).build()
        VideosAdapter(
            requireActivity(),
            onSearchHasTag = { searchFor(it) },
            onOpenProfile = { openProfile(it) },
            onComments = { openComments(it.first, it.second) },
            onToggleFollow = { toggleFollow(it) },
            onFavorite = { addFavorite(it) },
            myImage = getUserImage(),
            viewModel = viewModel,
            fragment = this,
//            exoPlayer1 = exoPlayer,
//            mCacheDataSourceFactory,
        )
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

    private fun openComments(id: Int, num: Int) {
        findNavController().navigate(
            R.id.commentsFragment,
            bundleOf(CommentsFragment.POST_ID to id, CommentsFragment.NUM_COMMENTS to num)
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
        Log.e("iiiiiii","package name onViewCreated " )

        requireActivity().window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

//        setUpExoPlayer()

        binding.apply {
            swipeRefresh.setOnRefreshListener {
                videosAdapter.refresh()
            }

            observeFlow()


//            rec.adapter = videosAdapter

            viewPager2.adapter = videosAdapter
            viewPager2.offscreenPageLimit = 3
//            viewPager2.setHasTransientState(false)
            viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)

//                    ProfileFragmentFragment.userId = VideosAdapter.chefsId
//                    IngedientsFragment.tutorial_id = VideosAdapter.tutorial_id


//                    Log.e("positionviewPager2"," position "+VideosAdapter.chefsId)

//                    Log.e("onResume", " positionviewPager2  " + VideosAdapter.chefsId)

                    videosAdapter.currentPosition = position

                    videosAdapter.setUserAndTutorialId(position)

//                    videosAdapter.currentPosition = position
//                    videosAdapter.notifyItemChanged(position)

                    for (index in 0..videosAdapter.viewHolderList.size.minus(1)) {
                        if (index == position)
                            videosAdapter.viewHolderList[index].item.idExoPlayerVIew.player?.play()
                        else videosAdapter.viewHolderList[index].item.idExoPlayerVIew.player?.pause()
                    }

//                    videosAdapter.playVideoAt(position )
//                    ProfileFragmentFragment.userId=videosAdapter.getItemAt(position)

//                    viewModel.singleVideo(videosAdapter.getItemAt(position)!!)



                    if (viewModel.isLogin(requireContext()).not()) {
                        if (position >= 4) {
                            if (activity is BaseActivity) {
                                (activity as BaseActivity).apply {
                                    videosAdapter.removeVideo()
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
                    Log.e(
                        TAG,
                        "onPageScrolled: $position , $positionOffset , $positionOffsetPixels"
                    )

                }

                override fun onPageScrollStateChanged(state: Int) {
                    super.onPageScrollStateChanged(state)
                    Log.e(TAG, "onPageScrollStateChanged: $state")
                //                    if (state == 1)
//                        videosAdapter.dim()
//                    else videosAdapter.reset()
                }
            })


        }
        setAdapterStates()
    }

//    private fun setUpExoPlayer() {
//        mHttpDataSourceFactory = DefaultHttpDataSource.Factory()
//            .setAllowCrossProtocolRedirects(true)
//
//        this.mDefaultDataSourceFactory = DefaultDataSourceFactory(
//            requireContext(), mHttpDataSourceFactory
//        )
//
//        mCacheDataSourceFactory = CacheDataSource.Factory()
//            .setCache(cache)
//            .setUpstreamDataSourceFactory(mHttpDataSourceFactory)
//            .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)
//
//        exoPlayer = SimpleExoPlayer.Builder(requireContext())
//            .setMediaSourceFactory(DefaultMediaSourceFactory(mCacheDataSourceFactory)).build()
//
//    }

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
                        videosAdapter.removeVideo()

                        binding.swipeRefresh.isRefreshing = true

                    }
                    is LoadState.Error -> {
//                        setEmpty(false)
                        videosAdapter.removeVideo()

                        binding.swipeRefresh.isRefreshing = false

                        handleErrorGeneral((loadStates.refresh as LoadState.Error).error)
                    }
                }
            }
        }
    }

    private fun observeFlow() {


//        handleSharedFlow(viewModel.singleVideo, onSuccess = {
//            Log.e("ttttt", " singleVideoooo " + it)
//
//            singleVideo.getSingleVideo(it as TutorialVideos)
////            videosAdapter.getSingleVideo(item = ItemVedioPlayerBinding.bind(binding.root),it as TutorialVideos)
//
//        })

        handleSharedFlow(viewModel.toggleFollow, onSuccess = {

            Log.e("fffffff", " msg " + it)
            Toast.makeText(requireContext(), " " + it, Toast.LENGTH_SHORT).show()

        })

        handleSharedFlow(viewModel.addFavorite, onSuccess = {

        })

        lifecycleScope.launchWhenStarted {
            viewModel.list.collectLatest {
                Log.e("randomList", " list " + videosAdapter.viewHolderList)
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
        Log.e("iiiiiii","package name onPause " )
        videosAdapter.pauseVideo()
//        videosAdapter.playVideoAt(-1)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.e("iiiiiii","package name onDestroyView " )
        videosAdapter.removeVideo()
    }

    override fun onResume() {
        super.onResume()
//       if( videosAdapter.itemCount>0){
//           viewModel.singleVideo(videosAdapter.getItemAt(0)!!)
//       }

//        lifecycleScope.launchWhenStarted {
//            viewModel.list.collectLatest {
//                videosAdapter.submitData(it)
//            }
//        }
    }


//    private fun cacheVideo(
//        dataSpec: DataSpec,
//        defaultCacheKeyFactory: CacheKeyFactory?,
//        dataSource: DataSource,
//        progressListener: CacheUtil.ProgressListener
//    ) {
//        CacheUtil.cache(
//            dataSpec,
//            simpleCache,
//            defaultCacheKeyFactory,
//            dataSource,
//            progressListener,
//            null
//        )
//    }


}