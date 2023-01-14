package com.chefshub.app.presentation.splash

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.lifecycle.lifecycleScope
import com.chefshub.app.R
import com.chefshub.app.presentation.main.MainActivity
import com.chefshub.base.BaseActivity
import kotlinx.coroutines.delay

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFullScreen()
        setContentView(R.layout.activity_splash)


        lifecycleScope.launchWhenStarted {
            delay(1000)
            findViewById<MotionLayout>(R.id.motionLayout).transitionToEnd {

                startActivity(MainActivity::class.java)
                finishAffinity()
            }
        }

    }

}