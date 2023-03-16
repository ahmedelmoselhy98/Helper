package com.chefshub.app.presentation.main_video

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.chefshub.app.presentation.main.ui.ingrediants.IngedientsFragment
import com.chefshub.app.presentation.main_video.profile.ProfileFragmentFragment
import com.chefshub.app.presentation.main_video.video.VediosPlayerFragment
import com.facebook.FacebookSdk

class MainPagerAdapter(fragmentActivity: Fragment) :
    FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount() = 3

    override fun createFragment(position: Int): Fragment {
        Log.e("iiiiiii","package name " )
        return when (position) {
            2 -> ProfileFragmentFragment()
            1 -> VediosPlayerFragment()
            else -> IngedientsFragment()
        }
    }
}