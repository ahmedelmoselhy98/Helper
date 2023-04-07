package com.chefshub.app.presentation.main_video.profile

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.chefshub.app.R
import com.chefshub.app.databinding.ItemVedioPlayerBinding
import com.chefshub.base.BaseActivity
import com.chefshub.base.BaseFragment
import com.chefshub.data.entity.tutorial.TutorialModel
import com.chefshub.utils.ext.loadImage
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import createDynamicLink
import shareDeepLink

class ChefListVideosAdapter(
    private var context: Context,
    private val onComments: (Pair<Int, Int>) -> Unit,
    private val onToggleFollow: (id: Int) -> Unit,
    private val onFavorite: (id: Int) -> Unit,
    private val fragment: BaseFragment,
) : RecyclerView.Adapter<ChefListVideosAdapter.ViewHolder>() {

    private val videoList = ArrayList<TutorialModel>()
    private var playbackPosition = 0L
    private var currentWindow = 0
    private var tvFavState = false


    inner class ViewHolder(val item: ItemVedioPlayerBinding) : RecyclerView.ViewHolder(item.root) {

        var player: ExoPlayer? = null

        fun bind() {
            item.bookMark.isVisible = false
            Log.e("mashal", "bind: ${videoList[adapterPosition].url!!}")
            val data = videoList[adapterPosition]
            tvFavState = data?.isFavourites!!

            if (data?.chef?.isFollowing!!) {
                item.btnFollow.text = context.getText(R.string.following)
                item.btnFollow.setTextColor(Color.GREEN)
            } else {
                item.btnFollow.setTextColor(Color.WHITE)
                item.btnFollow.text = context.getText(R.string.follow)
            }

            item.tvUsername.text = data?.chef?.name
            item.ivUserImage.loadImage(data?.chef?.avatarPath)
            item.tvComments.text = data.commentsCount.toString()
            item.viewCount.text = data.views.toString()

            item.tvFav.text = data.favouritesCount.toString()
            item.tvMessage.text = data.sharesCount.toString()

            item.tvTitle.text = data?.title
            item.tvDescription.text = data?.caption

            if (player == null) {
                player = ExoPlayer.Builder(item.root.context).build()
                item.idExoPlayerVIew.player = player
            }

            val dataSourceFactory = DefaultDataSourceFactory(itemView.context, "my-video-player")
            val mediaItem = MediaItem.fromUri(videoList[adapterPosition].url!!)
            val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(mediaItem)
            player?.setMediaSource(mediaSource)
            player?.prepare()

            // Set the player's playback position based on the current visible position of the recycler view
            if (adapterPosition == currentWindow) {
                player?.seekTo(playbackPosition)
//                player?.playWhenReady = true
            } else {
                player?.pause()
            }
        }

        init {
            item.apply {
                tvMessage.setOnClickListener {
                    createDynamicLink(
                        context as BaseActivity,
                        videoList.get(bindingAdapterPosition)?.id.toString()
                    ) { dynamicLink ->
                        fragment.shareDeepLink(dynamicLink)
                    }
                }


                tvFav.setOnClickListener {
                    if (bindingAdapterPosition == -1) return@setOnClickListener
                    videoList.get(bindingAdapterPosition)?.id?.let { it1 -> onFavorite.invoke(it1) }
                    var num: Int = tvFav.text.toString().toInt()
                    if (tvFavState) {
                        tvFavState = false
                        item.tvFav.setCompoundDrawablesWithIntrinsicBounds(
                            0,
                            R.drawable.ic_heart,
                            0,
                            0
                        )
                        tvFav.text = num.minus(1).toString()
                    } else {
                        tvFavState = true
                        tvFav.text = (num + 1).toString()
                        item.tvFav.setCompoundDrawablesWithIntrinsicBounds(
                            0,
                            R.drawable.filled_heart,
                            0,
                            0
                        )
                    }
                }

                tvComments.setOnClickListener {
                    if (bindingAdapterPosition == -1) return@setOnClickListener
                    videoList.get(bindingAdapterPosition)?.id?.let { it1 ->
                        onComments.invoke(
                            Pair(
                                it1,
                                tvComments.text.toString().toInt()
                            )
                        )
                    }
                }

                btnFollow.setOnClickListener {
                    if (bindingAdapterPosition == -1) return@setOnClickListener
                    videoList.get(bindingAdapterPosition)?.chef?.id.let { it1 -> onToggleFollow.invoke(it1!!) }
                    if (btnFollow.text == it.context.getString(R.string.follow)) {
                        btnFollow.text = it.context.getString(R.string.following)
                        item.btnFollow.setTextColor(Color.GREEN)
                    } else {
                        btnFollow.text = it.context.getString(R.string.follow)
                        item.btnFollow.setTextColor(Color.WHITE)
                    }
                }

                item.root.setOnClickListener {
                    if (item.idExoPlayerVIew.player?.isPlaying == true) {
                        item.idExoPlayerVIew.player?.pause()
                    } else {
                        item.idExoPlayerVIew.player?.play()
                    }
                }
            }
        }
        fun releasePlayer() {
            player?.release()
            player = null
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemVedioPlayerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount() = videoList.size

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
//        holder.releasePlayer()
    }

    fun setCurrentWindow(newPosition: Int) {
        if (newPosition != currentWindow) {
            playbackPosition = 0L
            currentWindow = newPosition
            notifyDataSetChanged()
        }
    }

    fun setAll(it: ArrayList<TutorialModel>) {
        this.videoList.apply {
            clear()
            addAll(it)
        }
        notifyDataSetChanged()
    }
}

//class ChefListVideosAdapter : RecyclerView.Adapter<ChefListVideosAdapter.ViewHolder>() {
//
//    private val videoList = ArrayList<TutorialModel>()
//    private var playbackPosition = 0L
//    private var currentWindow = 0
//
//    inner class ViewHolder(val item: ItemVedioPlayerBinding) : RecyclerView.ViewHolder(item.root) {
//
//        var player: ExoPlayer? = null
//
//        fun bind() {
//            item.bookMark.isVisible = false
//
//            if (player == null) {
//                player = ExoPlayer.Builder(item.root.context).build()
//                item.idExoPlayerVIew.player = player
//            }
//
//            val dataSourceFactory = DefaultDataSourceFactory(itemView.context, "my-video-player")
//            val mediaItem = MediaItem.fromUri(videoList[adapterPosition].url!!)
//            val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(mediaItem)
//            player?.setMediaSource(mediaSource)
//
//            // Set the player's playback position based on the current visible position of the recycler view
//            if (adapterPosition == currentWindow) {
//                player?.seekTo(playbackPosition)
//                player?.playWhenReady = true
//            } else {
//                player?.pause()
//            }
//        }
//
//        init {
//            item.root.setOnClickListener {
//                Navigation.findNavController(it).navigate(
//                    R.id.ChefVideoFragment,
//                    bundleOf("url" to videoList[bindingAdapterPosition].url.toString())
//                )
//            }
//        }
//
//        fun releasePlayer() {
//            player?.release()
//            player = null
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
//        ItemVedioPlayerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//    )
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.bind()
//    }
//
//    override fun getItemCount() = videoList.size
//
//    override fun onViewRecycled(holder: ViewHolder) {
//        super.onViewRecycled(holder)
//        holder.releasePlayer()
//    }
//
//    fun setCurrentWindow(newPosition: Int) {
//        if (newPosition != currentWindow) {
//            playbackPosition = 0L
//            currentWindow = newPosition
//            notifyDataSetChanged()
//        }
//    }
//
//    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//        super.onScrolled(recyclerView, dx, dy)
//
//        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
//        val firstVisiblePosition = layoutManager.findFirstVisibleItemPosition()
//
//        // Update the playback position and current window if the first visible position has changed
//        if (firstVisiblePosition != currentWindow) {
//            val player = (recyclerView.findViewHolderForAdapterPosition(currentWindow) as? ViewHolder)?.player
//            if (player != null) {
//                playbackPosition = player.currentPosition
//                player.pause()
//            }
//
//            currentWindow = firstVisiblePosition
//            val newPlayer = (recyclerView.findViewHolderForAdapterPosition(currentWindow) as? ViewHolder)?.player
//            if (newPlayer != null) {
//                newPlayer.seekTo(playbackPosition)
//                newPlayer.playWhenReady = true
//            }
//        }
//    }
//
//    fun setAll(it: ArrayList<TutorialModel>) {
//        this.videoList.apply {
//            clear()
//            addAll(it)
//        }
//        notifyDataSetChanged()
//    }
//}



//class ChefListVideosAdapter : RecyclerView.Adapter<ChefListVideosAdapter.ViewHolder>() {
//
//    private val videoList = ArrayList<TutorialModel>()
//    private var playbackPosition = 0L
//    private var currentWindow = 0
//
//    inner class ViewHolder(val item: ItemVedioPlayerBinding) : RecyclerView.ViewHolder(item.root) {
//
//
//
//        fun bind() {
//            item.bookMark.isVisible = false
//            val player = ExoPlayer.Builder(item.root.context).build()
//            item.idExoPlayerVIew.player = player
//
//            val dataSourceFactory = DefaultDataSourceFactory(itemView.context, "my-video-player")
//            val mediaItem = MediaItem.fromUri(videoList[adapterPosition].url!!)
//            val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(mediaItem)
//            player.prepare(mediaSource)
//
//            // Set the player's playback position based on the current visible position of the recycler view
//            if (position == currentWindow) {
//                player?.seekTo(playbackPosition)
//                player?.prepare(mediaSource)
//                player?.play()
//            } else {
//                player?.stop()
//                player?.clearMediaItems()
//            }
//        }
//
//        init {
//            item.root.setOnClickListener {
//                Navigation.findNavController(it).navigate(
//                    R.id.ChefVideoFragment,
//                    bundleOf("url" to videoList.get(bindingAdapterPosition).url.toString())
//                )
//            }
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
//        ItemVedioPlayerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//    )
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.bind()
//    }
//
//    override fun getItemCount() = videoList.size
//
//    fun setAll(it: ArrayList<TutorialModel>) {
//        this.videoList.apply {
//            clear()
//            addAll(it)
//        }
//        notifyDataSetChanged()
//    }
//}