package com.eldenbuild.ui.build_detail_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.eldenbuild.R
import com.eldenbuild.databinding.FragmentBuildDetailBinding
import com.eldenbuild.ui.builds_overview_fragment.BuildsOverviewFragmentDirections
import com.eldenbuild.util.AppViewModelProvider
import com.eldenbuild.viewmodel.CurrentBuild
import com.google.android.material.carousel.CarouselSnapHelper
import kotlinx.coroutines.launch


class BuildDetailFragment : Fragment() {
    private var _binding: FragmentBuildDetailBinding? = null
    val binding get() = _binding!!
    private val sharedViewModel: BuildDetailViewModel by activityViewModels {
        AppViewModelProvider.Factory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_build_detail,
                container,
                false
            )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            viewModel = sharedViewModel
            lifecycleOwner = viewLifecycleOwner

        }
        lifecycleScope.launch {

            CurrentBuild.buildDetail.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect { build ->
                    // bind the currentBuild data to the views
                    binding.currentBuild = build
                    // check if the buildItems list is empty or not
                    build?.let {
                        if (it.buildItems.isNotEmpty()) {
                            binding.itemSelectionGridRecycler.visibility = View.VISIBLE
                            binding.placeholderImage.visibility = View.GONE
                            binding.placeholderTextView.visibility = View.GONE
                        } else {
                            binding.placeholderImage.visibility = View.VISIBLE
                            binding.placeholderTextView.visibility = View.VISIBLE
                        }
                    }
                }
        }

        binding.itemSelectionGridRecycler.adapter = BuildItemsGridAdapter {}

        binding.itemSelectionCarousel.adapter = CarouselAdapter {
//            sharedViewModel.setItem(Items.WEAPON)
            findNavController().navigate(
                BuildsOverviewFragmentDirections
                    .actionBuildsOverviewFragmentToCustomizeBuildFragment(it)
            )
        }
        val snapHelper = CarouselSnapHelper()
        snapHelper.attachToRecyclerView(binding.itemSelectionCarousel)
        super.onViewCreated(view, savedInstanceState)
    }
}