package com.chefshub.app.presentation.main.ui.ingrediants

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.chefshub.app.databinding.FragmentIngedientsBottomSheetBinding
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IngedientsVideoFragmentBottomSheet : BottomSheetDialogFragment() {
    private var _binding: FragmentIngedientsBottomSheetBinding? = null

    private val binding get() = _binding!!

    private var exoPlayer: ExoPlayer? =null
    private var playbackPosition = 0L
    private var playWhenReady = true


    companion object {

        fun newInstance(myString: String): IngedientsVideoFragmentBottomSheet {
            val args = Bundle()
            args.putString("url", myString)

            val fragment = IngedientsVideoFragmentBottomSheet()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentIngedientsBottomSheetBinding.inflate(layoutInflater)
        return binding.root
//        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e("bottomsheet", "onViewCreated: ")
//        _binding = FragmentVideoIngedientsBottomsheetBinding.bind(view)

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
            Log.e("videoUrl ", " videoURLuuuuu " + videoURL)

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