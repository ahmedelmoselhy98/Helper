package com.chefshub.app.presentation.main.ui.singleVideo

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.chefshub.app.R
import com.chefshub.app.databinding.FragmentVideoSingleBinding
import com.chefshub.app.presentation.main.ui.vedios.TutorialViewModel
import com.chefshub.base.BaseFragment
import com.chefshub.data.entity.bookmarked.VideoModel
import com.chefshub.data.entity.tutorial.TutorialVideos
import com.chefshub.utils.ext.loadImage
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "SingleVideoLoadFragment"
@AndroidEntryPoint
class SingleVideoLoadFragment : BaseFragment(R.layout.fragment_video_single) {
    private var _binding: FragmentVideoSingleBinding? = null

    private val binding get() = _binding

    private var exoPlayer: ExoPlayer? =null
    private var playbackPosition = 0L
    private var playWhenReady = true
    private val viewModel: TutorialViewModel by activityViewModels()

    companion object{
        var videoId :Int?=null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentVideoSingleBinding.bind(view)

        videoId?.let { viewModel.singleVideo(it) }

        binding?.let { binding ->
            setAction()
        }
    }

    private fun setAction() {

       binding?.apply {

            back.setOnClickListener {
                findNavController().navigateUp()
            }

            idExoPlayerVIew.setOnClickListener {
                if (idExoPlayerVIew.player?.isPlaying == true) {
                    idExoPlayerVIew.player?.pause()
                } else {
                    idExoPlayerVIew.player?.play()
                }
            }


            handleSharedFlow(viewModel.singleVideo, onSuccess = {
                Log.e(TAG, "onViewCreated: $it" )
                if (it is TutorialVideos) {


                   loadVideo(it.url)
                    tvFav.text = it.favouritesCount.toString()
                    tvComments.text =it.commentsCount.toString()
                    viewCount.text = it.views.toString()
                    tvUsername.text =it.chef?.name
                    ivUserImage.loadImage(it.chef?.avatarPath)
                    tvMessage.text = it.sharesCount.toString()
                    tvTitle.text =it.title
                    tvDescription.text =it.caption
                }
            })
            }
        }

    private fun loadVideo(uri: String?) {
        if (!uri.isNullOrEmpty()) {
            binding?.apply {


                exoPlayer = ExoPlayer.Builder(requireContext()).build()
                val uri: Uri =
                    Uri.parse(uri)
                val mediaItem: MediaItem =
                    MediaItem.fromUri(uri)

                idExoPlayerVIew.player = exoPlayer
                idExoPlayerVIew.player?.repeatMode = Player.REPEAT_MODE_ALL
                exoPlayer?.addMediaItem(mediaItem)

                exoPlayer?.seekTo(playbackPosition)
                exoPlayer?.playWhenReady = playWhenReady
                exoPlayer?.prepare()

            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        exoPlayer?.stop()
        exoPlayer?.clearMediaItems()
        exoPlayer?.release()
        exoPlayer = null
        _binding = null
    }

    override fun onPause() {
        super.onPause()
        exoPlayer?.pause()
    }


}