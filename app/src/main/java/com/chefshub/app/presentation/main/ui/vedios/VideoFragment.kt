package com.chefshub.app.presentation.main.ui.vedios

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.chefshub.app.R
import com.chefshub.app.databinding.FragmentVideoBinding
import com.chefshub.app.presentation.main.ui.ingrediants.IngedientsFragment
import com.chefshub.app.presentation.main_video.MainPagerAdapter
import com.chefshub.app.presentation.main_video.profile.ProfileFragmentFragment
import com.chefshub.app.presentation.main_video.video.VediosPlayerFragment
import com.chefshub.base.BaseFragment
import com.chefshub.utils.ext.reduceDragSensitivity
import com.facebook.FacebookSdk.getApplicationContext
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VideoFragment : BaseFragment(R.layout.fragment_video) {
    private var _binding: FragmentVideoBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentVideoBinding.bind(view)


        binding.viewPager2.apply {
            reduceDragSensitivity(8)
            adapter = MainPagerAdapter(this@VideoFragment)
            setCurrentItem(1, false)

            Log.e("iiiiiii","package name " +getApplicationContext().getPackageName())

        }

//        binding.viewPager2.registerOnPageChangeCallback(object :  ViewPager2.OnPageChangeCallback() {
//
//            override fun onPageScrolled(
//                position: Int,
//                positionOffset: Float,
//                positionOffsetPixels: Int
//            ) {
//                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
//
////                when (position ){
////                    2 ->
////                    {
////
////                        binding.viewPager2.setCurrentItem(2, false)
////                    }
//////                    1 ->{
//////                        if (binding.viewPager2.currentItem == 1)
//////                        binding.viewPager2.setCurrentItem(2, false)
//////                    else
//////                        binding.viewPager2.setCurrentItem(1, false)
//////                    }
////                    0 ->{
////                        if (binding.viewPager2.currentItem == 0)
////                        binding.viewPager2.setCurrentItem(1, false)
////                        else
////                        binding.viewPager2.setCurrentItem(0, false)
////                    }
////                }
//                        Log.e("nnnnnnnn"," onPageScrolled  "+position)
//
////                binding.viewPager2.setCurrentItem(position, false)
//
//            }
//
//            override fun onPageSelected(position: Int) {
//                super.onPageSelected(position)
//                Log.e("nnnnnnnn"," onPageSelected  "+position)
//
//            }
//
//
//            override fun onPageScrollStateChanged(state: Int) {
//                super.onPageScrollStateChanged(state)
////
//                Log.e("nnnnnnnn"," onPageScrollStateChanged "+state)
//            }
//
//        })
    }
}