package com.eldenbuild.ui.build_detail_fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.eldenbuild.R
import com.eldenbuild.databinding.FragmentBuildDetailBinding
import com.eldenbuild.ui.builds_overview_fragment.BuildsOverviewFragmentDirections
import com.eldenbuild.ui.item_detail_fragment.ItemDetailViewModel
import com.eldenbuild.util.AppViewModelProvider
import com.eldenbuild.util.Dialog
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
            repeatOnLifecycle(Lifecycle.State.STARTED) {


                launch {
                    sharedViewModel.checkedItemsUiState
                        .collect { list ->
                            binding.itemSelectionGridRecycler.adapter =
                                BuildItemsGridAdapter(true, checkList = list,
                                    checkedItems = { card, item, addItem ->

                                        if (addItem) {

                                            sharedViewModel.addCheckedItem(item)
                                        } else {

                                            sharedViewModel.removeCheckedItem(item)
                                        }
                                        card.isChecked = list.contains(item)
                                        Log.d("checkedItemList", "Items = ${list.size}")


                                        binding.buildTopAppBar?.setOnMenuItemClickListener { menuItem ->
                                            when (menuItem.itemId) {
                                                R.id.delete -> {
                                                    Dialog.buildDialog(
                                                        context = requireContext(),
                                                        message = getString(
                                                            R.string.delete_items_dialog_message,
                                                            list.size.toString()
                                                        ),
                                                        title = R.string.delete_items_dialog_title,
                                                        positiveActionText = getString(R.string.delete_text),
                                                        positiveAction = {
                                                            Toast.makeText(
                                                                requireContext(),
                                                                sharedViewModel.deleteCheckedItems(
                                                                    list
                                                                )
                                                                ,Toast.LENGTH_SHORT).show()
                                                        }

                                                    ).show()

                                                    true
                                                }

                                                else -> false
                                            }
                                        }

                                        binding.buildTopAppBar?.let { topBar ->
                                            topBar.menu.findItem(R.id.delete).isVisible =
                                                list.isNotEmpty()
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

                launch {

                    BuildDetailViewModel.buildDetail
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