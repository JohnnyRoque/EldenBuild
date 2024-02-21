package com.eldenbuild.ui.build_detail_fragment

import android.os.Bundle
import android.util.Log
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
import com.eldenbuild.ui.build_detail_fragment.BuildDetailViewModel.CurrentBuild.addCheckedItem
import com.eldenbuild.ui.build_detail_fragment.BuildDetailViewModel.CurrentBuild.removeCheckedItem
import com.eldenbuild.ui.builds_overview_fragment.BuildsOverviewFragmentDirections
import com.eldenbuild.ui.item_detail_fragment.ItemDetailViewModel
import com.eldenbuild.util.AppViewModelProvider
import com.google.android.material.carousel.CarouselSnapHelper
import kotlinx.coroutines.flow.filterNotNull
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

            BuildDetailViewModel.buildDetail.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .filterNotNull()
                .collect { build ->

                    // bind the currentBuild data to the views
                    binding.currentBuild = build
                    // check if the buildItems list is empty or not

                    if (build.buildItems.isNotEmpty()) {
                        binding.itemSelectionGridRecycler.visibility = View.VISIBLE
                        binding.placeholderImage.visibility = View.GONE
                        binding.placeholderTextView.visibility = View.GONE
                    } else {
                        binding.placeholderImage.visibility = View.VISIBLE
                        binding.placeholderTextView.visibility = View.VISIBLE
                    }

                }
        }
        lifecycleScope.launch {
            sharedViewModel.checkedItemsUiState.flowWithLifecycle(
                lifecycle,
                Lifecycle.State.STARTED
            )
                .collect { list ->
                    binding.itemSelectionGridRecycler.adapter = BuildItemsGridAdapter(true, checkList = list,
                        checkedItems = { card, item, addItem ->
                            list.run {

                                if (addItem) {
                                    addCheckedItem(item)
                                    Log.d("checkedItemList", "Item index = ${list.indexOf(item)}")

                                } else {
                                    removeCheckedItem(item)
                                }
                                card.isChecked = this.contains(item)

                                Log.d("checkedItemList", "Items = ${list.size}")

                            }


                        }) { item, itemType ->

                        ItemDetailViewModel.getCurrentItem(item)
                        findNavController().navigate(
                            BuildsOverviewFragmentDirections.actionBuildsOverviewFragmentToItemDetailsFragment(
                                type = itemType,
                                true
                            )
                        )
                        Log.d("itemType", "type = $itemType")

                    }
                }
        }

        binding.itemSelectionCarousel.adapter = CarouselAdapter {

            findNavController().navigate(
                BuildsOverviewFragmentDirections
                    .actionBuildsOverviewFragmentToCustomizeBuildFragment(it, true)
            )
        }
        val snapHelper = CarouselSnapHelper()
        snapHelper.attachToRecyclerView(binding.itemSelectionCarousel)
        super.onViewCreated(view, savedInstanceState)
    }
}