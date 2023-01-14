package com.chefshub.app.presentation.main_video.video

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.chefshub.app.R
import com.chefshub.app.databinding.ItemVedioPlayerBinding
import com.chefshub.app.presentation.main.ui.vedios.TutorialViewModel
import com.chefshub.base.BaseActivity
import com.chefshub.base.BaseFragment
import com.chefshub.data.entity.tutorial.TutorialModel
import com.chefshub.data.entity.tutorial.TutorialVideos
import com.chefshub.utils.ext.loadImage
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.volokh.danylo.hashtaghelper.HashTagHelper
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class VideosAdapter(
    private val context: Context,
    private val onSearchHasTag: (string: String) -> Unit,
    private val onOpenProfile: (id: Int) -> Unit,
    private val onComments: (Pair<Int, Int>) -> Unit,
    private val onToggleFollow: (id: Int) -> Unit,
    private val onFavorite: (id: Int) -> Unit,
    private val myImage: String?,
    private val viewModel: TutorialViewModel

) :
    PagingDataAdapter<TutorialModel, VideosAdapter.ViewHolder>(DIFF_UTILS) {

    private var selectedPosition = 0
    private var itemdata = TutorialVideos()


    private var exoPlayer: ExoPlayer? = null
    private var playbackPosition = 0L
    private var playWhenReady = true

    inner class ViewHolder(val item: ItemVedioPlayerBinding) : RecyclerView.ViewHolder(item.root) {
        init {

            item.apply {
                btnFollow.setOnClickListener {
                    if (bindingAdapterPosition == -1) return@setOnClickListener
                    getItem(bindingAdapterPosition)?.id?.let { it1 -> onToggleFollow.invoke(it1) }
                    if (btnFollow.text == it.context.getString(R.string.following))
                        btnFollow.text = it.context.getString(R.string.following)
                    else
                        btnFollow.text = it.context.getString(R.string.follow)
                }
                ivLoggedInUserImage.loadImage(myImage)

                linearUser.setOnClickListener {
                    getItem(bindingAdapterPosition)?.id?.let {
                        onOpenProfile.invoke(
                            it
                        )
                    }
                }
                val mTextHashTagHelper =
                    HashTagHelper.Creator.create(
                        ContextCompat.getColor(context, R.color.white)
                    ) {
                        onSearchHasTag.invoke(it)
                    }
                mTextHashTagHelper.handle(item.tvTitle)

                tvComments.setOnClickListener {
                    if (bindingAdapterPosition == -1) return@setOnClickListener
                    getItem(bindingAdapterPosition)?.id?.let { it1 -> onComments.invoke(Pair(it1,tvComments.text.toString().toInt())) }
                }

                tvFav.setOnClickListener {
                    if (bindingAdapterPosition == -1) return@setOnClickListener
                    getItem(bindingAdapterPosition)?.id?.let { it1 -> onFavorite.invoke(it1) }
                }


                item.idExoPlayerVIew.setOnClickListener {
                    if (idExoPlayerVIew.player?.isPlaying == true){
                        idExoPlayerVIewPause.isVisible=true
                        idExoPlayerVIew.player?.pause()
                    }
                    else{
                        idExoPlayerVIew.player?.play()
                        idExoPlayerVIewPause.isVisible=false
                    }
                }
            }
        }

        fun bind() {
            val data = getItem(bindingAdapterPosition)

            viewModel.singleVideo(data?.id!!)

//            item.tvUsername.text = itemdata?.chef?.name

            item.linearIcons.alpha = if (dim) .6f else 1f
            item.tvTitle.text = data?.title
            item.tvDescription.text = data?.caption

//            item.tvUsername.text = data?.chef?.name

//            item.btnFollow.text=data.
            if (selectedPosition == bindingAdapterPosition) {
                exoPlayer?.release()
                exoPlayer = ExoPlayer.Builder(item.root.context).build()

                Log.e("bbbbbbb", "  " + data)
                val videoURL =   data.url
//                    "https://media.geeksforgeeks.org/wp-content/uploads/20201217163353/Screenrecorder-2020-12-17-16-32-03-350.mp4"
                Log.e("bbbbbbb", " videoURL  " + videoURL)

                val uri: Uri = Uri.parse(videoURL)
                val mediaItem: MediaItem =
                    MediaItem.fromUri(uri)

                item.apply {
                    idExoPlayerVIew.player = exoPlayer
                    idExoPlayerVIew.player?.repeatMode = Player.REPEAT_MODE_ALL
                    exoPlayer?.setMediaItem(mediaItem)
                    exoPlayer?.seekTo(playbackPosition)
                    exoPlayer?.playWhenReady = playWhenReady
                    exoPlayer?.prepare()
                    exoPlayer?.play()
                }
            }
            GlobalScope.launch {
                (context as BaseActivity).handleSharedFlow(viewModel.singleVideo, onSuccess = {
                    Log.e("ttttt", " singleVideoooo " + it)
                    getSingleVideo(it as TutorialVideos)
                })
            }
        }


        fun getSingleVideo(data: TutorialVideos) {


            item.tvUsername.text = data?.chef?.name
            item.ivUserImage.loadImage(data?.chef?.avatarPath)
            item.tvComments.text = data.commentsCount.toString()

//                data.views.toString()
            item.tvFav.text =data.favouritesCount.toString()
            item.tvMessage.text = data.sharesCount.toString()

        }
    }

//    interface SingleVideo{
//        fun getSingleVideo(data: TutorialVideos)
//    }


    fun playVideoAt(position: Int) {
        try {
            val temp = selectedPosition
            selectedPosition = position
            kotlin.runCatching { notifyItemChanged(temp) }
            kotlin.runCatching { notifyItemChanged(position) }
        } catch (ex: Exception) {
            Log.e("hhhhhhh", "  " + ex.message)
            ex.printStackTrace()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemVedioPlayerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.setIsRecyclable(false)


        holder.bind()
    }

    private var dim = false
    fun dim() {
        dim = true
        notifyDataSetChanged()
    }

    fun reset() {
        dim = false
        notifyDataSetChanged()
    }

    fun getItemAt(position: Int) = getItem(position)?.id


}


val DIFF_UTILS = object : DiffUtil.ItemCallback<TutorialModel>() {
    override fun areItemsTheSame(oldItem: TutorialModel, newItem: TutorialModel) =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: TutorialModel, newItem: TutorialModel) =
        oldItem == newItem
}