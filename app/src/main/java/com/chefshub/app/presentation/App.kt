package com.chefshub.app.presentation

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.chefshub.app.BuildConfig
import com.example.di.APP_VERSION
import com.google.android.exoplayer2.database.ExoDatabaseProvider
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    companion object {
        lateinit var cache: SimpleCache
    }

    private val cacheSize: Long = 90 * 1024 * 1024
    private lateinit var cacheEvictor: LeastRecentlyUsedCacheEvictor
    private lateinit var exoplayerDatabaseProvider: ExoDatabaseProvider

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        APP_VERSION = BuildConfig.VERSION_NAME
        cacheEvictor = LeastRecentlyUsedCacheEvictor(cacheSize)
        exoplayerDatabaseProvider = ExoDatabaseProvider(this)
        cache = SimpleCache(cacheDir, cacheEvictor, exoplayerDatabaseProvider)
    }
}