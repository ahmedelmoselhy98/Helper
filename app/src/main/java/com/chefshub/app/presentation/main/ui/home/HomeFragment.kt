package com.chefshub.app.presentation.main.ui.home

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewTreeLifecycleOwner
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.chefshub.app.R
import com.chefshub.app.databinding.FragmentHomeBinding
import com.chefshub.app.presentation.main.ui.home.adapter.MealAdapter
import com.chefshub.app.presentation.main.ui.home.adapter.MostViewAdapter
import com.chefshub.app.presentation.main.ui.home.adapter.SearchCheifsAdapter
import com.chefshub.app.presentation.main.ui.home.adapter.SearchVideosAdapter
import com.chefshub.app.presentation.main.ui.ingrediants.IngedientsFragment
import com.chefshub.app.presentation.main.ui.singleVideo.SingleVideoLoadFragment
import com.chefshub.base.BaseFragment
import com.chefshub.data.entity.bookmarked.VideoModel
import com.chefshub.data.entity.search.MostViewResponse
import com.chefshub.data.entity.search.SearchResponse
import com.chefshub.utils.ext.loadImage
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


val TAG ="HomeFragment"
//@AndroidEntryPoint
class HomeFragment : BaseFragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null

    private val cheifsAdapter = SearchCheifsAdapter()
    private val mealAdapter = MealAdapter()
    private val mostViewAdapter = MostViewAdapter()
    private val searchVideosAdapter = SearchVideosAdapter() {

            findNavController().navigate(R.id.singleVideoFragment)

//        findNavController().navigate(R.id.FragmentVideoIngredients)

    }
    private val homeViewModel: HomeViewModel by activityViewModels()

    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)
        homeViewModel.mostViewByChefs()
        homeViewModel.getMostFamous()

        setUpAction()
        setupArguments()
        setupSearchListener()
        setupRecyclers()
        setupObserver()
        setupToolbar()
        setUpMeal()
    }

    private fun setUpAction() {
        binding.apply {
            meal.setOnClickListener {
                meal.setBackgroundColor(resources.getColor(R.color.blue1))
                chefs.setBackgroundColor(resources.getColor(R.color.blue2))
                recyclerViewChefs.isVisible=false
                recyclerViewVideos.isVisible=true
            }

            chefs.setOnClickListener {
                meal.setBackgroundColor(resources.getColor(R.color.blue2))
                chefs.setBackgroundColor(resources.getColor(R.color.blue1))
                recyclerViewChefs.isVisible=true
                recyclerViewVideos.isVisible=false
            }
        }
    }

    private fun setUpMeal() {
        val mealItems: List<String> = Arrays.asList(
            "breakfast",
            "lunch",
            "dinner",
            "snack"
        )
        mealAdapter.setAll(mealItems)
    }

    private fun setupObserver() {
        handleSharedFlow(homeViewModel.dataListFlow, onSuccess = {
            if (it is SearchResponse) {
                binding.meal.performClick()
                setupResponse(it)
            }
        })
        handleSharedFlow(homeViewModel.mostViewListFlow, onSuccess = {
                mostViewAdapter.setAll(it as List<MostViewResponse>)
        })
        handleSharedFlow(homeViewModel.mostFamousFlow, onSuccess = {
            if (it is VideoModel){
                var item :VideoModel= it
               binding.apply {
                   mealName.text=  item.title
                   discountImg.loadImage(item.screenshot_url)
               }
            }
        })
    }

    private fun setupResponse(it: SearchResponse) {
        binding.apply {
            disAccountCard.isVisible=false
            pickMealCard.isVisible=false
            chefsCard.isVisible=false
            recCard.isVisible= true
            filter.isVisible=true
        }
        it.chefs.let {
            cheifsAdapter.setAll(it)
//            binding.tvTitleChefs.isVisible = cheifsAdapter.itemCount > 0
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


}