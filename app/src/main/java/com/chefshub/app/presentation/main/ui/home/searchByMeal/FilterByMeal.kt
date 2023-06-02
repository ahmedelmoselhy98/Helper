package com.chefshub.app.presentation.main.ui.home.searchByMeal

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewTreeLifecycleOwner
import androidx.navigation.fragment.findNavController
import com.chefshub.app.R
import com.chefshub.app.databinding.FilterByMealBinding
import com.chefshub.app.databinding.FragmentHomeBinding
import com.chefshub.app.presentation.main.ui.home.HomeViewModel
import com.chefshub.app.presentation.main.ui.home.adapter.MealAdapter
import com.chefshub.app.presentation.main.ui.home.adapter.MostViewAdapter
import com.chefshub.app.presentation.main.ui.home.adapter.SearchCheifsAdapter
import com.chefshub.app.presentation.main.ui.home.adapter.SearchVideosAdapter
import com.chefshub.base.BaseFragment
import com.chefshub.data.entity.search.MealResponse
import com.chefshub.data.entity.search.MostViewResponse
import com.chefshub.data.entity.search.SearchResponse
import com.chefshub.data.entity.tutorial.TutorialVideos
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import java.util.*


val TAG ="HomeFragment"
@AndroidEntryPoint
class FilterByMeal : BaseFragment(R.layout.filter_by_meal) {

    private var _binding: FilterByMealBinding? = null

    private val cheifsAdapter = SearchCheifsAdapter()
    private val mealAdapter = MealAdapter()
    private val mostViewAdapter = MostViewAdapter()
    private val searchVideosAdapter = MealTypeAdapter() {
//        findNavController().navigate(R.id.videoFragment)
        findNavController().navigate(R.id.FragmentVideoIngredients)
    }
    private val homeViewModel: HomeViewModel by activityViewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FilterByMealBinding.bind(view)
        ViewTreeLifecycleOwner.get(view)

        homeViewModel.mealList(arguments?.getString("type")!!)
        binding.recyclerViewVideos.adapter = searchVideosAdapter

//        setupArguments()
//        setupSearchListener()
//        setupRecyclers()
        setupObserver()
//        setupToolbar()
//        setUpMeal()
    }


    private fun setupObserver() {
        handleSharedFlow(homeViewModel.mealListFlow, onSuccess = {
            searchVideosAdapter.setAll(it as ArrayList<MealResponse>)
            binding.emptyList.isVisible = if (it.isEmpty()) true else false
        })
    }

//    private fun setupToolbar() {
//        binding.toolbar.apply {
//            ivBack.setOnClickListener { findNavController().navigateUp() }
//            ivShare.isVisible = false
//            tvTitle.text = getString(R.string.search)
//        }
//    }

    override fun onDestroyView() {
        super.onDestroyView()
//        _binding = null
    }
}