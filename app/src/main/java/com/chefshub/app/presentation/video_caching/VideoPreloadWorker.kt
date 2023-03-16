package com.chefshub.app.presentation.video_caching

import android.content.Context
import android.net.Uri
import androidx.work.*
import com.chefshub.app.presentation.App
import com.google.android.exoplayer2.upstream.DataSpec
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.HttpDataSource
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.upstream.cache.CacheWriter
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class VideoPreloadWorker(private val context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {
    private var videoCachingJob: Job? = null
    private lateinit var mHttpDataSourceFactory: HttpDataSource.Factory
    private lateinit var mDefaultDataSourceFactory: DefaultDataSourceFactory
    private lateinit var mCacheDataSource: CacheDataSource
    private val cache: SimpleCache = App.cache

    companion object {
        const val VIDEO_URL = "video_url"

        fun buildWorkRequest(yourParameter: String): OneTimeWorkRequest {
            val data = Data.Builder().putString(VIDEO_URL, yourParameter).build()
            return OneTimeWorkRequestBuilder<VideoPreloadWorker>().apply { setInputData(data) }
                .build()
        }
    }


    override fun doWork(): Result {
        try {
//            val videoUrl: String? = inputData.getString("https://rr7---sn-uxaxjvhxbt2u-5atr.googlevideo.com/videoplayback?expire=1664581250&ei=Iio3Y6XVDvD2xgK0n7mIDA&ip=41.44.3.30&id=o-ALHPralLvD5Bo80TUfVrKXgjk_aACE5h6Lagz7dorjjw&itag=136&aitags=133%2C134%2C135%2C136%2C137%2C160%2C242%2C243%2C244%2C247%2C278&source=youtube&requiressl=yes&mh=su&mm=31%2C29&mn=sn-uxaxjvhxbt2u-5atr%2Csn-hgn7rn7k&ms=au%2Crdu&mv=m&mvi=7&pl=19&ctier=SH&initcwndbps=277500&spc=yR2vpw45LwE1jfWgCTd0yUmXdcGR2x-SIJPXjPlqYH4_&vprv=1&mime=video%2Fmp4&ns=nMCEPN3y7j_kxUiI9DndIb4I&gir=yes&clen=1898432&dur=24.733&lmt=1652175130373200&mt=1664559252&fvip=3&keepalive=yes&fexp=24001373%2C24007246&c=WEB&txp=6319224&n=P_jSR0V3ATuTPQ&sparams=expire%2Cei%2Cip%2Cid%2Caitags%2Csource%2Crequiressl%2Cctier%2Cspc%2Cvprv%2Cmime%2Cns%2Cgir%2Cclen%2Cdur%2Clmt&lsparams=mh%2Cmm%2Cmn%2Cms%2Cmv%2Cmvi%2Cpl%2Cinitcwndbps&lsig=AG3C_xAwRAIgDRvzoz9lbKObZS_6c-HhF7wAxxmyLcag74DK1EW8oC8CICl9DBsf158PXmiyNFrkwd-P18KjaK35M8BV8NXq4KjB&sig=AOq0QJ8wRQIgJ3L_386a93yYLoBvrGq0_g-eUeh9IxR6Q4v3Z9b5lvECIQCrtrq7kfbCOX-KPK5j5eY4IxJVQbDC6qV5BlLXhUgESQ%3D%3D&pot=D4POuQGJLvExg_Zf32IRjZaSUtNb9fTb99PdRJcmNm9c_5OsLOBcH9m_G6z_JXwfea3YRIXTpZT9tWkFAF0DthdjMVi9eggOo_xZoWwOCFMepCu6P0dbcc2WDR0%3D")
            val videoUrl: String? = inputData.getString(VIDEO_URL)

            mHttpDataSourceFactory = DefaultHttpDataSource.Factory()
                .setAllowCrossProtocolRedirects(true)

            mDefaultDataSourceFactory = DefaultDataSourceFactory(context, mHttpDataSourceFactory)

            mCacheDataSource = CacheDataSource.Factory()
                .setCache(cache)
                .setUpstreamDataSourceFactory(mHttpDataSourceFactory)
                .createDataSource()

            preCacheVideo(videoUrl)

            return Result.success()

        } catch (e: Exception) {
            return Result.failure()
        }
    }

    private fun preCacheVideo(videoUrl: String?) {

        val videoUri = Uri.parse(videoUrl)
        val dataSpec = DataSpec(videoUri)

        val progressListener = CacheWriter.ProgressListener { requestLength, bytesCached, _ ->
            val downloadPercentage: Double = (bytesCached * 100.0 / requestLength)
            // Do Something
        }

        videoCachingJob = GlobalScope.launch(Dispatchers.IO) {
            cacheVideo(dataSpec, progressListener)
            preCacheVideo(videoUrl)
        }
    }

    private fun cacheVideo(mDataSpec: DataSpec, mProgressListener: CacheWriter.ProgressListener) {
        runCatching {
            CacheWriter(mCacheDataSource, mDataSpec, null, mProgressListener).cache()
        }.onFailure {
            it.printStackTrace()
        }
    }
}