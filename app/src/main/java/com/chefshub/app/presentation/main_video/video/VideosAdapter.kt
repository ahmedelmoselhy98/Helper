package com.chefshub.app.presentation.main_video.video

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.TextureView
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import com.bumptech.glide.Glide
import com.chefshub.app.R
import com.chefshub.app.databinding.ItemVedioPlayerBinding
import com.chefshub.app.presentation.App
import com.chefshub.app.presentation.main.ui.ingrediants.IngedientsFragment
import com.chefshub.app.presentation.main.ui.vedios.TutorialViewModel
import com.chefshub.app.presentation.main_video.profile.ProfileFragmentFragment
import com.chefshub.app.presentation.video_caching.VideoPreloadWorker
import com.chefshub.base.BaseActivity
import com.chefshub.base.BaseFragment
import com.chefshub.data.entity.tutorial.TutorialModel
import com.chefshub.utils.ext.loadImage
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.HttpDataSource
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import com.volokh.danylo.hashtaghelper.HashTagHelper
import createDynamicLink
import dagger.hilt.android.qualifiers.ApplicationContext
import shareDeepLink


class VideosAdapter(
    private var context: Context,
    private val onSearchHasTag: (string: String) -> Unit,
    private val onOpenProfile: (id: Int) -> Unit,
    private val onComments: (Pair<Int, Int>) -> Unit,
    private val onToggleFollow: (id: Int) -> Unit,
    private val onFavorite: (id: Int) -> Unit,
    private val myImage: String?,
    private val viewModel: TutorialViewModel,
    private val fragment: BaseFragment,
//    private var exoPlayer1: ExoPlayer,
//    private var mCacheDataSourceFactory: DataSource.Factory


) : PagingDataAdapter<TutorialModel, VideosAdapter.ViewHolder>(DIFF_UTILS) {


//    private lateinit var mHttpDataSourceFactory: HttpDataSource.Factory
//    private lateinit var mDefaultDataSourceFactory: DefaultDataSourceFactory
//    private lateinit var mCacheDataSourceFactory: DataSource.Factory
//    private lateinit var exoPlayer: SimpleExoPlayer
//    private val cache: SimpleCache = App.cache

    private var selectedPosition = 0
    private var tvFavState = false
    var currentPosition = -1
    lateinit var currentViewHolder: ViewHolder
    var viewHolderList = ArrayList<ViewHolder>()


//    var item = TutorialVideos()

    companion object {
        var chefsId = 1
        var tutorial_id = 1
    }

    private var exoPlayer: ExoPlayer? = null

    private var playbackPosition = 0L
    private var playWhenReady = true


//    interface SingleVideo{
//        fun getSingleVideo(data: TutorialVideos)
//    }


//    fun updateVideo(postion:Int, item: TutorialVideos){
////        this.item = item
//        notifyItemChanged(postion , item)
//    }

    fun playVideoAt(position: Int) {
        try {
//            val temp = selectedPosition
//            selectedPosition = position
//            kotlin.runCatching { notifyItemChanged(temp) }
//            kotlin.runCatching { notifyItemChanged(position) }
        } catch (ex: Exception) {
            Log.e("hhhhhhh", "  " + ex.message)
            ex.printStackTrace()
        }
    }

    fun removeVideo() {

        if (!viewHolderList.isNullOrEmpty())
            viewHolderList[currentPosition].item.idExoPlayerVIew.player?.stop()
    }

      fun setUserAndTutorialId(position: Int) {
          Log.e("getusername","getuser "+getItem(position)?.chef?.name)

          ProfileFragmentFragment.userId =  getItem(position)?.chef?.id
          ProfileFragmentFragment.name =  getItem(position)?.chef?.name
          ProfileFragmentFragment.userimage =  getItem(position)?.chef?.avatarPath
          IngedientsFragment.tutorial_id = getItem(position)?.tutorial_id
          IngedientsFragment.ingredients_id = getItem(position)?.id
          IngedientsFragment.background = getItem(position)?.url
//              IngedientsFragment.tutorial_id
    }

    fun pauseVideo() {
        if (!viewHolderList.isNullOrEmpty())
            viewHolderList[currentPosition].item.idExoPlayerVIew.player?.pause()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemVedioPlayerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.setIsRecyclable(false)

//        if (position == 0)
//            viewModel.singleVideo(getItem(position)?.id!!)

//        holder.item.let {
//            Log.e("getpostionat", " onBindViewHolder  " + position)

//        }
        viewHolderList.add(holder)
//        if (currentPosition == position) {
//            currentViewHolder = holder
//        }
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

    inner class ViewHolder(val item: ItemVedioPlayerBinding) : RecyclerView.ViewHolder(item.root) {
        init {
            Log.e(
                "getdata",
                " ViewHolder position data  " + getItem(bindingAdapterPosition + 1)?.id
            )
            item.apply {
                tvMessage.setOnClickListener {
                    createDynamicLink(
                        context as BaseActivity,
                        getItem(bindingAdapterPosition)?.id.toString()
                    ) { dynamicLink ->
                        fragment.shareDeepLink(dynamicLink)

                    }
                }
//                ivLoggedInUserImage.loadImage(myImage)
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

                btnFollow.setOnClickListener {
                    if (bindingAdapterPosition == -1) return@setOnClickListener
                    getItem(bindingAdapterPosition)?.chef?.id.let { it1 -> onToggleFollow.invoke(it1!!) }
                    if (btnFollow.text == it.context.getString(R.string.follow)) {
                        btnFollow.text = it.context.getString(R.string.following)
                        item.btnFollow.setTextColor(Color.GREEN)
                    } else {
                        btnFollow.text = it.context.getString(R.string.follow)
                        item.btnFollow.setTextColor(Color.WHITE)
                    }
                }
                tvFav.setOnClickListener {
                    if (bindingAdapterPosition == -1) return@setOnClickListener
                    getItem(bindingAdapterPosition)?.id?.let { it1 -> onFavorite.invoke(it1) }
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
                    getItem(bindingAdapterPosition)?.id?.let { it1 ->
                        onComments.invoke(
                            Pair(
                                it1,
                                tvComments.text.toString().toInt()
                            )
                        )
                    }
                }
                item.idExoPlayerVIew.setOnClickListener {
                    if (idExoPlayerVIew.player?.isPlaying == true) {
                        idExoPlayerVIewPause.isVisible = true
                        idExoPlayerVIew.player?.pause()
                    } else {
                        idExoPlayerVIew.player?.play()
                        idExoPlayerVIewPause.isVisible = false
                    }
                }
            }
        }

        fun bind() {
            val data = getItem(bindingAdapterPosition)
            Log.e("onResume", " adapter position data  " + data)
//            Log.e("getdata111", " adapter position data  " + getItem(bindingAdapterPosition+1))
            var url = getItem(bindingAdapterPosition)?.url
            Glide.with(context).load(url).preload()
            tvFavState = data?.isFavourites!!
//            chefsId = data.chef?.id!!
//            tutorial_id = data.tutorial_id!!
            if (data.isFavourites!!) {
                item.tvFav.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    R.drawable.filled_heart,
                    0,
                    0
                );
            } else
                item.tvFav.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    R.drawable.ic_heart,
                    0,
                    0
                );

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

//                data.views.toString()
            item.tvFav.text = data.favouritesCount.toString()
            item.tvMessage.text = data.sharesCount.toString()


//            item.linearIcons.alpha = if (dim) .5f else 1f
            item.tvTitle.text = data?.title
            item.tvDescription.text = data?.caption

//            item.tvUsername.text = data?.chef?.name

//            item.btnFollow.text=data.
//            if (selectedPosition == bindingAdapterPosition) {
//                playWhenReady=false
//                exoPlayer?.playWhenReady=false
//                item.idExoPlayerVIew.player?.pause()
//            exoPlayer?.release()
            exoPlayer = ExoPlayer.Builder(context).build()
            val videoURL = data?.url
//                    "https://media.geeksforgeeks.org/wp-content/uploads/20201217163353/Screenrecorder-2020-12-17-16-32-03-350.mp4"
            Log.e("videoUrl ", " videoURL " + videoURL)
//                exoPlayer?.release()
            val uri: Uri =
                Uri.parse(videoURL)
            val mediaItem: MediaItem =
                MediaItem.fromUri(uri)
            item.apply {
                idExoPlayerVIew.player = exoPlayer
                idExoPlayerVIew.player?.repeatMode = Player.REPEAT_MODE_ALL
                exoPlayer?.addMediaItem(mediaItem)
//                exoPlayer?.seekTo(playbackPosition)
//                exoPlayer?.playWhenReady = playWhenReady
//                    exoPlayer?.pause()
//                    exoPlayer?.playWhenReady = true
                exoPlayer?.prepare()
//                    exoPlayer?.play()
//                if (bindingAdapterPosition == currentPosition) {
//                    exoPlayer?.play()
//                } else {
//                    exoPlayer?.pause()
//                }
//                }
            }
//            if (selectedPosition == bindingAdapterPosition) {
//
//                schedulePreloadWork(data.url!!)
////                exoPlayer.release()
//                setVideo(data.url!!, item.idExoPlayerVIew)
//
//            }
        }

        private fun schedulePreloadWork(videoUrl: String) {
            val workManager = WorkManager.getInstance(context.applicationContext)
            val videoPreloadWorker = VideoPreloadWorker.buildWorkRequest(videoUrl)
            workManager.enqueueUniqueWork(
                "VideoPreloadWorker",
                ExistingWorkPolicy.KEEP,
                videoPreloadWorker
            )
        }


//        fun getSingleVideo(data: TutorialVideos) {
////            Log.e("my item ", " my item video  " + data)
//
//            Log.e("getpostionat", " getSingleVideo  ")
//
////            item.linearIcons.isVisible = false
////            Handler().postDelayed({
////                item.linearIcons.isVisible = true
//////                item.linearIcons.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in))
////            }, 800)
//
//
//
//
////                var tvFavState = data.isFavourites!!
////
////                chefsId = data.chef?.id!!
////
////
////                if (data.isFavourites!!) {
////                    item.tvFav.setCompoundDrawablesWithIntrinsicBounds(
////                        0,
////                        R.drawable.filled_heart,
////                        0,
////                        0
////                    );
////                } else
////                    item.tvFav.setCompoundDrawablesWithIntrinsicBounds(
////                        0,
////                        R.drawable.ic_heart,
////                        0,
////                        0
////                    );
////
////                if (data.chef?.isFollowing!!) {
////                    item.btnFollow.text = context.getText(R.string.following)
////                    item.btnFollow.setTextColor(Color.GREEN)
////                } else {
////                    item.btnFollow.setTextColor(Color.WHITE)
////                    item.btnFollow.text = context.getText(R.string.follow)
////                }
////
////                item.tvUsername.text = data?.chef?.name
////                item.ivUserImage.loadImage(data?.chef?.avatarPath)
////                item.tvComments.text = data.commentsCount.toString()
////                item.viewCount.text = data.views.toString()
////
//////                data.views.toString()
////                item.tvFav.text = data.favouritesCount.toString()
////                item.tvMessage.text = data.sharesCount.toString()
////
////
//////            item.linearIcons.alpha = if (dim) .5f else 1f
////                item.tvTitle.text = data?.title
////                item.tvDescription.text = data?.caption
////
//////            item.tvUsername.text = data?.chef?.name
////
//////            item.btnFollow.text=data.
////                if (selectedPosition == bindingAdapterPosition) {
//                    exoPlayer?.release()
////                    exoPlayer = ExoPlayer.Builder(item.root.context).build()
////
////                    val videoURL = data?.url
//////                    "https://media.geeksforgeeks.org/wp-content/uploads/20201217163353/Screenrecorder-2020-12-17-16-32-03-350.mp4"
////                    Log.e("videoUrl ", " videoURL " + videoURL)
////
////
////                    val uri: Uri =
////                        Uri.parse(videoURL)
////                    val mediaItem: MediaItem =
////                        MediaItem.fromUri(uri)
////
////                    item.apply {
////                        idExoPlayerVIew.player = exoPlayer
////                        idExoPlayerVIew.player?.repeatMode = Player.REPEAT_MODE_ALL
////                        exoPlayer?.setMediaItem(mediaItem)
////                        exoPlayer?.seekTo(playbackPosition)
////                        exoPlayer?.playWhenReady = playWhenReady
////                        exoPlayer?.prepare()
////                        exoPlayer?.playWhenReady = true
////                        exoPlayer?.play()
////                    }
////                }
////
////                item.apply {
////
////
////                    btnFollow.setOnClickListener {
////                        if (bindingAdapterPosition == -1) return@setOnClickListener
////                        data?.chef?.id.let { it1 -> onToggleFollow.invoke(it1!!) }
////                        if (btnFollow.text == it.context.getString(R.string.follow)) {
////                            btnFollow.text = it.context.getString(R.string.following)
////                            item.btnFollow.setTextColor(Color.GREEN)
////                        } else {
////                            btnFollow.text = it.context.getString(R.string.follow)
////                            item.btnFollow.setTextColor(Color.WHITE)
////                        }
////                    }
////
////                    tvFav.setOnClickListener {
////                        if (bindingAdapterPosition == -1) return@setOnClickListener
////                        getItem(bindingAdapterPosition)?.id?.let { it1 -> onFavorite.invoke(it1) }
////                        var num: Int = tvFav.text.toString().toInt()
////                        if (tvFavState) {
////                            tvFavState = false
////                            item.tvFav.setCompoundDrawablesWithIntrinsicBounds(
////                                0,
////                                R.drawable.ic_heart,
////                                0,
////                                0
////                            )
////                            tvFav.text = num.minus(1).toString()
////                        } else {
////                            tvFavState = true
////                            tvFav.text = (num + 1).toString()
////                            item.tvFav.setCompoundDrawablesWithIntrinsicBounds(
////                                0,
////                                R.drawable.filled_heart,
////                                0,
////                                0
////                            )
////                        }
////                    }
////                }
//              }

    }

    private fun setVideo(videoUrl: String, idExoPlayerVIew: StyledPlayerView) {
//        mHttpDataSourceFactory = DefaultHttpDataSource.Factory()
//            .setAllowCrossProtocolRedirects(true)
//
//        this.mDefaultDataSourceFactory = DefaultDataSourceFactory(
//            context, mHttpDataSourceFactory)
//
//        mCacheDataSourceFactory = CacheDataSource.Factory()
//            .setCache(cache)
//            .setUpstreamDataSourceFactory(mHttpDataSourceFactory)
//            .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)


//        exoPlayer.release()

//        exoPlayer = SimpleExoPlayer.Builder(context)
//            .setMediaSourceFactory(DefaultMediaSourceFactory(mCacheDataSourceFactory)).build()

//        val videoUri = Uri.parse(videoUrl)
//        val mediaItem = MediaItem.fromUri(videoUri)
//        val mediaSource =
//            ProgressiveMediaSource.Factory(mCacheDataSourceFactory).createMediaSource(mediaItem)

//        idExoPlayerVIew.player = exoPlayer1
//        exoPlayer1.playWhenReady = true
//        exoPlayer1.seekTo(0, 0)
//        exoPlayer1.setMediaSource(mediaSource, true)
//        exoPlayer1.prepare()
    }


}


val DIFF_UTILS = object : DiffUtil.ItemCallback<TutorialModel>() {
    override fun areItemsTheSame(oldItem: TutorialModel, newItem: TutorialModel) =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: TutorialModel, newItem: TutorialModel) =
        oldItem == newItem
}