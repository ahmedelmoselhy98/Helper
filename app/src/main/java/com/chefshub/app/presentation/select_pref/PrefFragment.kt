package com.chefshub.app.presentation.select_pref

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.chefshub.app.R
import com.chefshub.app.databinding.CircleTextViewBinding
import com.chefshub.app.databinding.FragmentPrefBinding
import com.chefshub.app.presentation.main.MainActivity
import com.chefshub.base.BaseActivity
import com.chefshub.base.BaseFragment
import com.chefshub.data.entity.food_system.FoodSystemModel
import com.chefshub.utils.ext.setBackgroundTint
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.min
import kotlin.random.Random


@AndroidEntryPoint
class PrefFragment : BaseFragment(R.layout.fragment_pref) {

    private val arrayColor =
        listOf<Int>(
            R.color.purple_700,
            R.color.purple_500,
            R.color.red,
            R.color.green,
            R.color.purple_700,
            R.color.purple_500,
            R.color.red,
            R.color.green,
            R.color.purple_700,
            R.color.purple_500,
            R.color.red,
            R.color.green
        )
    private var _binding: FragmentPrefBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PrefViewModel by viewModels()

    private val listOfSelections = HashSet<String>()
    private fun setListToTextViewSelection() {
        binding.tvSelection.text = listOfSelections.joinToString()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPrefBinding.bind(view)


        if (arguments == null)
            viewModel.getSystemFood()
        else
            viewModel.getRegionalCuisines()

        observeFlow()
        binding.apply {
            btnNext.setOnClickListener {
                if (arguments == null)
                    findNavController().navigate(R.id.prefFragment, bundleOf("data" to true))
                else
                    if (activity is BaseActivity?)
                        (activity as BaseActivity).apply {
                            val intent = Intent(_context, MainActivity::class.java).apply {
                                putExtra("desc", R.id.videoFragment)
                            }
                            startActivity(intent)
                            finishAffinity()
                        }
            }
        }
    }

    private fun observeFlow() {
        handleSharedFlow(viewModel.foodSystemModel, onSuccess = {
            if (it !is ArrayList<*>) return@handleSharedFlow
            if (it.isNullOrEmpty()) return@handleSharedFlow
            if (it.get(0) !is FoodSystemModel) return@handleSharedFlow
            setupPrefrs(it as ArrayList<FoodSystemModel>)
        })
    }

    private fun setupPrefrs(arrayList: ArrayList<FoodSystemModel>) {
        if (_binding == null) return
        with(binding) {
            val listOfPref = listOf<CircleTextViewBinding>(
                tvCenter,
                pref1,
                pref2,
                pref3,
                pref4,
                pref5,
                pref6,
                pref7,
                pref8
            )

            for (i in 0..(min(listOfPref.size, arrayList.size).minus(1))) {

                listOfPref.get(i).let {
                    it.root.isVisible = true
                    it.tv.text = arrayList[i].name
                    it.tv.setBackgroundTint(arrayColor.get(Random.nextInt(arrayColor.size.minus(1))))
//                    it.tv.setBackgroundTint(R.color.gray)

                    it.tv.setOnClickListener { view ->
//                        view.setBackgroundTint(arrayColor.get(Random.nextInt(arrayColor.size.minus(1))))
                        view.setBackgroundTint(R.color.gray)
                        listOfSelections.add(it.tv.text.toString())
                        setListToTextViewSelection()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}