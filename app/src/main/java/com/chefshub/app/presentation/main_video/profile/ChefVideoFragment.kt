package com.chefshub.app.presentation.main_video.profile

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.chefshub.app.R
import com.chefshub.app.databinding.FragmentIngedientsBinding
import com.chefshub.app.databinding.FragmentVideoIngedientsBinding
import com.chefshub.app.presentation.main_video.profile.ProfileVideosAdapter
import com.chefshub.app.presentation.select_pref.PrefViewModel
import com.chefshub.base.BaseFragment
import com.chefshub.data.entity.tutorial.TutorialModel
import com.chefshub.data.entity.user.UserModel
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChefVideoFragment : BaseFragment(R.layout.fragment_video_ingedients) {
    private var _binding: FragmentVideoIngedientsBinding? = null

    private val binding get() = _binding!!

    private var exoPlayer: ExoPlayer? =null
    private var playbackPosition = 0L
    private var playWhenReady = true


//    private val viewModel: IngredientsViewModel by viewModels()
//    private val ingredientsAdapter = IngredientsAdapter()
//


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentVideoIngedientsBinding.bind(view)

//        viewModel.getIngredients(7)


        observeFlow()
        setAction()
    }

    private fun setAction() {
        with(binding) {

            back.setOnClickListener {
                findNavController().navigateUp()
            }

            idExoPlayerVIew.setOnClickListener {
                if (idExoPlayerVIew.player?.isPlaying == true) {
//                    idExoPlayerVIewPause.isVisible = true
                    idExoPlayerVIew.player?.pause()
                } else {
                    idExoPlayerVIew.player?.play()
//                    idExoPlayerVIewPause.isVisible = false
                }
            }

            exoPlayer = ExoPlayer.Builder(requireContext()).build()

            val videoURL = arguments?.get("url").toString()
//                    "https://media.geeksforgeeks.org/wp-content/uploads/20201217163353/Screenrecorder-2020-12-17-16-32-03-350.mp4"
            Log.e("videoUrl ", " videoURL " + videoURL)


            val uri: Uri =
                Uri.parse(videoURL)
            val mediaItem: MediaItem =
                MediaItem.fromUri(uri)



                idExoPlayerVIew.player = exoPlayer
                idExoPlayerVIew.player?.repeatMode = Player.REPEAT_MODE_ALL
                exoPlayer?.addMediaItem(mediaItem)

                exoPlayer?.seekTo(playbackPosition)
                exoPlayer?.playWhenReady = playWhenReady
                exoPlayer?.prepare()
//                    exoPlayer?.playWhenReady = true
//                    exoPlayer?.play()
            }
        }

    private fun observeFlow() {
//        handleSharedFlow(viewModel.ingredientsFlow, onSuccess = {
//            Log.e("ingredientsFlow"," it "+it)
//            ingredientsAdapter.setAll(it as ArrayList<TutorialModel>)
//        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        exoPlayer?.release()
        _binding = null
    }
}