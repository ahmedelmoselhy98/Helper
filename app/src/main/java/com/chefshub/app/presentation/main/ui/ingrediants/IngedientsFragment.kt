package com.chefshub.app.presentation.main.ui.ingrediants

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.chefshub.app.R
import com.chefshub.app.databinding.FragmentIngedientsBinding
import com.chefshub.app.presentation.main_video.profile.ProfileVideosAdapter
import com.chefshub.app.presentation.main_video.video.VediosPlayerFragment
import com.chefshub.app.presentation.select_pref.PrefViewModel
import com.chefshub.base.BaseFragment
import com.chefshub.data.entity.ingredients.IngredientsModel
import com.chefshub.data.entity.tutorial.TutorialModel
import com.chefshub.data.entity.user.UserModel
import com.chefshub.utils.ext.DateExt
import com.chefshub.utils.ext.loadImage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IngedientsFragment : BaseFragment(R.layout.fragment_ingedients) {
    private var _binding: FragmentIngedientsBinding? = null

    private val binding get() = _binding!!

    private val viewModel: IngredientsViewModel by viewModels()

    //    private val ingredientsAdapter = IngredientsAdapter()
    private val cookingStepsAdapter = CookingStepsAdapter({ onclicked(it) })
    private val ingredientsAdapter = RecepsIngredientsAdapter()

    fun onclicked(url:String) {
        val bottomSheetFragment = IngedientsVideoFragmentBottomSheet.newInstance(url)
        bottomSheetFragment.show(requireFragmentManager(), bottomSheetFragment.tag)
    }

    companion object {
        var tutorial_id: Int? = null
        var ingredients_id: Int? = null
        var background: String? = null
        var totalAmount: Int? = 1
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentIngedientsBinding.bind(view)

        if (tutorial_id != null && ingredients_id != null) {
            viewModel.getCookingSteps(tutorial_id!!)
            viewModel.getIngredients1(ingredients_id!!)
        }

        binding.recyclerView.apply {
            setHasFixedSize(true)
            adapter = ingredientsAdapter
        }
        observeFlow()
        setAction()
    }

    private fun setAction() {
        with(binding) {
            recipeBackground.loadImage(background)
            ivCart.setOnClickListener { findNavController().navigate(R.id.ordersFragment) }
            back.setOnClickListener {
                Navigation.findNavController(it).navigate(R.id.videoFragment)
            }

            plus.setOnClickListener {
                totalAmount = totalAmount?.plus(1)
                amount.text = totalAmount.toString()
                ingredientsAdapter.notifyDataSetChanged()
            }
            minus.setOnClickListener {
                if (totalAmount!! > 1) {
                    totalAmount = totalAmount?.minus(1)
                    amount.text = totalAmount.toString()
                    ingredientsAdapter.notifyDataSetChanged()
                }
            }

            ingredients.setOnClickListener {
                linearIngredients.isVisible = true
                linearCooking.isVisible = false

                binding.recyclerView.adapter = ingredientsAdapter

                binding.recyclerView.isVisible = true

                it.setBackgroundResource(R.drawable.shape_round_15_white)
                ingredients.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue1))
                cookingSteps.setTextColor(ContextCompat.getColor(requireContext(), R.color.black1))

                cookingSteps.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(), R.color.white1
                    )
                )
            }

            cookingSteps.setOnClickListener {
                linearIngredients.isVisible = false
                linearCooking.isVisible = true
                cookingStepsAdapter.step = 1
                it.setBackgroundResource(R.drawable.shape_round_15_white)

                binding.recyclerView.adapter = cookingStepsAdapter

                binding.recyclerView.isVisible = true

                cookingSteps.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue1))
                ingredients.setTextColor(ContextCompat.getColor(requireContext(), R.color.black1))

                ingredients.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(), R.color.white1
                    )
                )
            }
        }
    }

    private fun observeFlow() {
        handleSharedFlow(viewModel.ingredientsFlow1, onSuccess = {
            ingredientsAdapter.setAll(it as ArrayList<IngredientsModel>)
            Log.e("ingredientsFlow", " it " + ingredientsAdapter.itemCount)
            binding.ingredientsCount.text = ingredientsAdapter.itemCount.toString()
        })

        handleSharedFlow(viewModel.cookingStepsFlow, onSuccess = {
            cookingStepsAdapter.setAll(it as ArrayList<TutorialModel>)
            Log.e("ingredientsFlow", " it " + cookingStepsAdapter.itemCount)
            binding.stepsCount.text = cookingStepsAdapter.itemCount.toString()
        })
    }

    override fun onResume() {
        super.onResume()
        binding.recipeBackground.loadImage(background)

        viewModel.getIngredients1(ingredients_id!!)
        viewModel.getCookingSteps(tutorial_id!!)
    }

    override fun onDestroyView() {
        super.onDestroyView()
//        _binding = null
    }
}