package com.chefshub.app.presentation.main.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewTreeLifecycleOwner
import androidx.navigation.fragment.findNavController
import com.chefshub.app.R
import com.chefshub.app.databinding.FragmentHomeBinding
import com.chefshub.base.BaseFragment
import com.chefshub.data.entity.search.SearchResponse

//@HiltAndroidApp
class HomeFragment : BaseFragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null

    private val cheifsAdapter = HomeCheifsAdapter()
    private val homeVideosAdapter = HomeVideosAdapter() {
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
            homeVideosAdapter.setAll(it)
        }
        binding.tvEmpty.isVisible = cheifsAdapter.itemCount == 0 && homeVideosAdapter.itemCount == 0
    }

    private fun setupRecyclers() {
        binding.apply {
            recyclerViewChefs.adapter = cheifsAdapter
            recyclerViewVideos.adapter = homeVideosAdapter
        }
    }

    private fun setupSearchListener() {
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