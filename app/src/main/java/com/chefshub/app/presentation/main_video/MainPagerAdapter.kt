package com.chefshub.app.presentation.main_video

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.chefshub.app.presentation.main.ui.ingrediants.IngedientsFragment
import com.chefshub.app.presentation.main_video.profile.ProfileFragmentFragment
import com.chefshub.app.presentation.main_video.video.VediosPlayerFragment

class MainPagerAdapter(fragmentActivity: Fragment) :
    FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount() = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            2 -> ProfileFragmentFragment()
            1 -> VediosPlayerFragment()
            else -> IngedientsFragment()
        }
    }
}