package com.chefshub.app.presentation.main

import android.animation.Animator
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.chefshub.app.R
import com.chefshub.app.databinding.ActivityMainBinding
import com.chefshub.app.presentation.main_video.profile.ProfileFragmentFragment
import com.chefshub.base.BaseActivity
import com.chefshub.utils.ext.loadImage
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

private const val DURATION_ANIMATION = 200L

@AndroidEntryPoint
class MainActivity : BaseActivity(), Animator.AnimatorListener {

    private lateinit var binding: ActivityMainBinding
    private var navController: NavController? = null
    private val mainViewModel by viewModels<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.


        navView.setupWithNavController(navController!!)
        navView.itemIconTintList = null

        navController?.addOnDestinationChangedListener { controller, destination, arguments ->
            if (destination.id in listOf(
                    R.id.videoFragment,
//                    R.id.navigation_home,
//                    R.id.navigation_dashboard,
//                    R.id.chatFragment,
//                    R.id.profileFragmentFragment
                    R.id.checkoutFragment,
                    R.id.ordersFragment,
                    R.id.dialogSuccessOrder,
                    R.id.commentsFragment
                )
            ) {
//                hideNavView()
                hideToolbar()

            } else {
//                showNavViewIfPossible()

//                showToolbarIfPossible()

            }
        }

//        navView.setOnNavigationItemSelectedListener { menuItem ->
//            when (menuItem.itemId) {
//                R.id.videoFragment -> {
//                    // Refresh data for nav item one
//
//                    true
//                }
////                R.id.nav_item_two -> {
////                    // Refresh data for nav item two
////                    true
////                }
//                // Add more nav items as needed
//                else -> false
//            }
//        }

        if (intent.hasExtra("desc")) {
            navController?.navigate(intent.getIntExtra("desc", R.id.navigation_home))
            lifecycleScope.launchWhenStarted {
                delay(200)
//                showNavViewIfPossible()
                showToolbarIfPossible()
            }
        } else {
            lifecycleScope.launchWhenStarted {
                delay(200)
                hideToolbar()
//                hideNavView()
            }
        }

        setupToolbar()
    }


    private fun forceHideBars() {
        binding.apply {
            appBarLayout2.isVisible = false
//            navView.isVisible = false
        }
    }

    private fun hideNavView() {
        binding.navView.apply {
            if (translationY != 0f)
                return
            animate().translationY(height.toFloat()).setDuration(DURATION_ANIMATION)
                .setStartDelay(DURATION_ANIMATION)
                .start()
        }
    }

    private fun showNavViewIfPossible() {
        binding.navView.apply {
            if (translationY == 0f)
                return
            animate().translationY(0f).setDuration(DURATION_ANIMATION)
                .setStartDelay(DURATION_ANIMATION)
                .start()
        }
    }

    ///////
    private fun hideToolbar() {
        binding.appBarLayout2.apply {
            if (translationY != 0f)
                return
            animate().translationY(height.toFloat() * -1).setDuration(DURATION_ANIMATION)
                .setStartDelay(DURATION_ANIMATION)
                .setListener(this@MainActivity)
                .start()
        }
    }

    private fun showToolbarIfPossible() {
        binding.appBarLayout2.apply {
            if (translationY == 0f)
                return
            animate().translationY(0f).setDuration(DURATION_ANIMATION)
                .setStartDelay(DURATION_ANIMATION)
                .setListener(this@MainActivity)
                .start()
        }
    }

    override fun onAnimationStart(animation: Animator) {
        if (binding.appBarLayout2.translationY != 0f) {
            binding.apply {
                appBarLayout2.isVisible = true
//                navView.isVisible = true
            }
        }
    }

    override fun onAnimationEnd(animation: Animator) {
        if (binding.appBarLayout2.translationY != 0f) {
            binding.apply {
                appBarLayout2.isVisible = false
//                navView.isVisible = false
            }
        }

    }


    override fun onAnimationCancel(animation: Animator) {

    }

    override fun onAnimationRepeat(animation: Animator) {

    }

    override fun onBackPressed() {
        if (navController == null) finish()
        if (navController?.navigateUp()?.not() == true)
            super.onBackPressed()
    }

    private fun setupToolbar() {
        binding.ivProfile.loadImage(mainViewModel.getUserProfile())
        binding.ivProfile.setOnClickListener { navigateToMyProfile() }
    }

    private fun navigateToMyProfile() {
        ProfileFragmentFragment.userId = null
        navController?.navigate(R.id.profileFragmentFragment)
    }
}