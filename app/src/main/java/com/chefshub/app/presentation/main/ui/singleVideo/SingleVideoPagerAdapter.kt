package com.chefshub.app.presentation.main.ui.singleVideo

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.chefshub.app.presentation.main.ui.ingrediants.IngedientsFragment

class SingleVideoPagerAdapter(fragmentActivity: Fragment) :
    FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {

        return when (position) {
            1 -> SingleVideoLoadFragment()
            else -> IngedientsFragment()
        }
    }
}