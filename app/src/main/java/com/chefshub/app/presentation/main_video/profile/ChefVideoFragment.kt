package com.chefshub.app.presentation.main_video.profile

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chefshub.app.R
import com.chefshub.app.databinding.FragmentVideoChefBinding
import com.chefshub.app.presentation.main.ui.vedios.TutorialViewModel
import com.chefshub.app.presentation.main_video.comments.CommentsFragment
import com.chefshub.app.presentation.main_video.video.VideosAdapter
import com.chefshub.base.BaseFragment
import com.chefshub.data.entity.tutorial.TutorialModel
import com.google.android.exoplayer2.ExoPlayer
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChefVideoFragment : BaseFragment(R.layout.fragment_video_chef) {
    private var _binding: FragmentVideoChefBinding? = null

    private val binding get() = _binding!!

    private var exoPlayer: ExoPlayer? =null
    private var playbackPosition = 0L
    private var playWhenReady = true
    private val userViewModel: ProfileViewModel by viewModels()
    private val viewModel: TutorialViewModel by activityViewModels()



    val listvideosChef by lazy {
        ChefListVideosAdapter(
            requireActivity(),
            onComments = { openComments(it.first, it.second) },
            onToggleFollow = { toggleFollow(it) },
            onFavorite = { addFavorite(it) },
            fragment = this
        )
    }

    private fun openComments(id: Int, num: Int) {
        findNavController().navigate(
            R.id.commentsFragment,
            bundleOf(CommentsFragment.POST_ID to id, CommentsFragment.NUM_COMMENTS to num)
        )
    }

    private fun toggleFollow(it: Int) {
        viewModel.toggleFollow(it)
    }

    private fun addFavorite(it: Int) {
        viewModel.addFavorite(it)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentVideoChefBinding.bind(view)

        userViewModel.getTutorialsVideosChef(ProfileFragmentFragment.userId!!)

//        viewModel.getIngredients(7)
        setupRecyclerView()

        setViewPager()


        observeFlow()
        setAction()
    }

    private fun setupRecyclerView() {
        binding.recVideo.adapter = listvideosChef
        binding.recVideo.layoutManager = LinearLayoutManager(requireContext())

        binding.recVideo.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val firstVisiblePosition = layoutManager.findFirstVisibleItemPosition()

                    listvideosChef.setCurrentWindow(firstVisiblePosition)
                }
            }
        })
    }


    private fun setViewPager() {
        binding.apply {

            val layoutManager = LinearLayoutManager(requireContext())
            recVideo.layoutManager = layoutManager
            recVideo.adapter = listvideosChef


    }
    }

    private fun setAction() {
        with(binding) {

            back.setOnClickListener {
                findNavController().navigateUp()
            }

//            idExoPlayerVIew.setOnClickListener {
//                if (idExoPlayerVIew.player?.isPlaying == true) {
////                    idExoPlayerVIewPause.isVisible = true
//                    idExoPlayerVIew.player?.pause()
//                } else {
//                    idExoPlayerVIew.player?.play()
////                    idExoPlayerVIewPause.isVisible = false
//                }
//            }

//            exoPlayer = ExoPlayer.Builder(requireContext()).build()
//
//            val videoURL = arguments?.get("url").toString()
////                    "https://media.geeksforgeeks.org/wp-content/uploads/20201217163353/Screenrecorder-2020-12-17-16-32-03-350.mp4"
//            Log.e("videoUrl ", " videoURL " + videoURL)
//
//
//            val uri: Uri =
//                Uri.parse(videoURL)
//            val mediaItem: MediaItem =
//                MediaItem.fromUri(uri)
//
//                idExoPlayerVIew.player = exoPlayer
//                idExoPlayerVIew.player?.repeatMode = Player.REPEAT_MODE_ALL
//                exoPlayer?.addMediaItem(mediaItem)
//
//                exoPlayer?.seekTo(playbackPosition)
//                exoPlayer?.playWhenReady = playWhenReady
//                exoPlayer?.prepare()

//                    exoPlayer?.playWhenReady = true
//                    exoPlayer?.play()
            }
        }

    private fun observeFlow() {
        handleSharedFlow(userViewModel.VideosChefFlow, onSuccess = {
            Log.e("ingredientsFlow"," it "+it)
            listvideosChef.setAll(it as ArrayList<TutorialModel>)
        })

    }


    override fun onDestroyView() {
        super.onDestroyView()
        exoPlayer?.release()
        _binding = null
    }
}