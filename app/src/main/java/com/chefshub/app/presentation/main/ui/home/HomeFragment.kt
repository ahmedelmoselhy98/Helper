package com.chefshub.app.presentation.main.ui.home

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewTreeLifecycleOwner
import androidx.navigation.fragment.findNavController
import com.chefshub.app.R
import com.chefshub.app.databinding.FragmentHomeBinding
import com.chefshub.base.BaseFragment
import com.chefshub.data.entity.search.SearchResponse
import java.util.*
import kotlin.collections.ArrayList


//@HiltAndroidApp
class HomeFragment : BaseFragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null

    private val cheifsAdapter = SearchCheifsAdapter()
    private val mealAdapter = MealAdapter()
    private val mostViewAdapter = MostViewAdapter()
    private val searchVideosAdapter = SearchVideosAdapter() {
        findNavController().navigate(R.id.videoFragment)
    }
    private val homeViewModel: HomeViewModel by activityViewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)
        ViewTreeLifecycleOwner.get(view)

        setupArguments()
        setupSearchListener()
        setupRecyclers()
        setupObserver()
        setupToolbar()
        setUpMeal()
    }

    private fun setUpMeal() {
        val mealItems: List<String> = Arrays.asList(
            "Breakfast",
            "Brunch",
            "Breakfast",
            "Breakfast",
        )
        mealAdapter.setAll(mealItems)

    }

    private fun setupObserver() {
        handleSharedFlow(homeViewModel.dataListFlow, onSuccess = {
            if (it is SearchResponse) {
                setupResponse(it)
            }
        })
    }

    private fun setupResponse(it: SearchResponse) {
        it.chefs.let {
            cheifsAdapter.setAll(it)
            binding.tvTitleChefs.isVisible = cheifsAdapter.itemCount > 0
        }
        it.tutorials.let {
            searchVideosAdapter.setAll(it)
        }
        binding.tvEmpty.isVisible = cheifsAdapter.itemCount == 0 && searchVideosAdapter.itemCount == 0
    }

    private fun setupRecyclers() {
        binding.apply {
            recyclerViewChefs.adapter = cheifsAdapter
            recyclerViewVideos.adapter = searchVideosAdapter
            recMeal.adapter = mealAdapter
            recMostView.adapter =mostViewAdapter
        }
    }

    private fun setupToolbar() {
        binding.toolbar.apply {
            ivBack.setOnClickListener { findNavController().navigateUp() }
            ivShare.isVisible = false
            tvTitle.text = getString(R.string.search)
        }
    }

    private fun setupSearchListener() {

        binding.edtSearch.isIconified = false // set the SearchView to be open by default

        binding.edtSearch.isQueryRefinementEnabled=true

        binding.edtSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                homeViewModel.searchFor(binding.edtSearch.query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun setupArguments() {
        if (arguments?.containsKey("value") == false) return
        arguments?.getString("value")?.let {
            binding.edtSearch.setQuery(it, true)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}