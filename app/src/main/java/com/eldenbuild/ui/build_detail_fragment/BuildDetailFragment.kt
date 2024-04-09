package com.eldenbuild.ui.build_detail_fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.eldenbuild.R
import com.eldenbuild.databinding.FragmentBuildDetailBinding
import com.eldenbuild.ui.builds_overview_fragment.BuildsOverviewFragmentDirections
import com.eldenbuild.ui.item_detail_fragment.ItemDetailViewModel
import com.eldenbuild.util.Dialog
import com.google.android.material.carousel.CarouselSnapHelper
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class BuildDetailFragment : Fragment() {
    private var _binding: FragmentBuildDetailBinding? = null
    val binding get() = _binding!!
    private val sharedViewModel: BuildDetailViewModel by viewModel()


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

        val adapter = BuildItemsGridAdapter(
            isItemSelectable = true,
            checkedItemList = listOf(),
            checkedItems = { card, item, addItem ->

                if (addItem) {
                    sharedViewModel.addCheckedItem(item)
                    card.isChecked = true

                } else {
                    sharedViewModel.removeCheckedItem(item)
                    card.isChecked = false

                }

            }, openItemDetail = { item, itemType ->

                ItemDetailViewModel.getCurrentItem(item)
                findNavController().navigate(
                    BuildsOverviewFragmentDirections
                        .actionBuildsOverviewFragmentToItemDetailsFragment(
                            type = itemType,
                            true
                        )
                )
                Log.d("itemType", "type = $itemType")

            })


        binding.apply {
            viewModel = sharedViewModel
            lifecycleOwner = viewLifecycleOwner
            itemSelectionGridRecycler.adapter = adapter
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    sharedViewModel.checkedItemUiState.collect { list ->

                        adapter.checkedItemList = list

                        binding.buildTopAppBar?.let { topBar ->

                            topBar.menu.findItem(R.id.delete).isVisible = list.isNotEmpty()
                            topBar.setOnMenuItemClickListener { menuItem ->
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
                                                adapter.notifyItemRangeRemoved(
                                                    adapter.position,
                                                    list.size
                                                )

                                                adapter.notifyItemRangeChanged(
                                                    adapter.position,
                                                    adapter.itemCount
                                                )

                                                Toast.makeText(
                                                    requireContext(),
                                                    sharedViewModel.deleteCheckedItems(
                                                        list
                                                    ), Toast.LENGTH_SHORT
                                                ).show()



                                            }
                                        )
                                        true
                                    }

                                    else -> false
                                }
                            }
                        }
                        Log.d("checkedItemList", "Count = ${list.size}")
                    }
                }

                launch {
                    BuildDetailViewModel.buildDetail.filterNotNull().collect { build ->

                        // bind the currentBuild data to the views
                        binding.currentBuild = build

                        Log.d("buildSize", "${build.buildItems.size}")

                        // check if the buildItems list is empty or not
                        if (build.buildItems.isNotEmpty()) {
                            binding.itemSelectionGridRecycler.visibility = View.VISIBLE
                            binding.placeholderImage.visibility = View.GONE
                            binding.placeholderTextView.visibility = View.GONE

                        } else {
                            binding.itemSelectionGridRecycler.visibility = View.INVISIBLE
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