package com.eldenbuild.ui.customize_build_fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.eldenbuild.R
import com.eldenbuild.databinding.FragmentCustomizeBuildBinding
import com.eldenbuild.ui.build_detail_fragment.BuildItemsGridAdapter
import com.eldenbuild.ui.builds_overview_fragment.TAG
import com.eldenbuild.ui.item_detail_fragment.ItemDetailViewModel
import com.eldenbuild.util.AppViewModelProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val CUSTOMIZE_LABEL = "customizeLabel"

class CustomizeBuildFragment : Fragment() {
    private var argumentLabel: String? = null
    private var _binding: FragmentCustomizeBuildBinding? = null
    val binding get() = _binding!!
    private val sharedViewModel: CustomizeBuildViewModel by activityViewModels {
        AppViewModelProvider.Factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        argumentLabel = arguments?.getString(CUSTOMIZE_LABEL)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_customize_build,
                container,
                false
            )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.apply {
            viewModel = sharedViewModel
            label = argumentLabel
            lifecycleOwner = viewLifecycleOwner
        }

        val itemAdapter = BuildItemsGridAdapter {
            ItemDetailViewModel.getCurrentItem(it)
            findNavController().navigate(
                CustomizeBuildFragmentDirections.actionCustomizeBuildFragmentToItemDetailsFragment()
            )
        }

        binding.itemRecyclerView.adapter = itemAdapter

        binding.statusRecyclerView.adapter = BuildStatusAdapter(
            isEditAttributes = false
        ) { attName, isInc ->
//            sharedViewModel.setNewAttribute(attName, isInc)
        }

        lifecycleScope.launch {

            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    sharedViewModel.currentListWeapons.collect {
                        if (it.isEmpty()) {
                            binding.loadingItemsAnimation.visibility = View.VISIBLE
                        } else {
                            itemAdapter.submitList(it)
                            binding.loadingItemsAnimation.visibility = View.GONE
                            binding.itemRecyclerView.visibility = View.VISIBLE
                        }
                    }
                }

                when (argumentLabel) {

                    "Equipment" -> {
                        binding.bottomNavigation.setOnItemSelectedListener { item ->

                            when (item.itemId) {

                                R.id.page_1 -> {
                                    launch(Dispatchers.IO) {
                                        sharedViewModel.currentListWeapons.collect {
                                            itemAdapter.submitList(it)
                                        }
                                    }
                                    Log.d(TAG, "PAGE1")
                                    true
                                }

                                R.id.page_2 -> {

                                    launch(Dispatchers.IO) {
                                        sharedViewModel.currentListArmors.collect {
                                            itemAdapter.submitList(it)

                                        }
                                    }
                                    Log.d(TAG, "PAGE2")
                                    true
                                }

                                R.id.page_3 -> {
                                    launch(Dispatchers.IO) {
                                        sharedViewModel.currentListShields.collect {
                                            itemAdapter.submitList(it)

                                        }
                                    }
                                    true
                                }


                                R.id.page_4 -> {
                                    launch(Dispatchers.IO) {
                                        sharedViewModel.currentListTalismans.collect {
                                            itemAdapter.submitList(it)

                                        }
                                    }
                                    true
                                }

                                else -> false
                            }
                        }

                        binding.bottomNavigation.setOnItemReselectedListener { item ->
                            when (item.itemId) {
                                R.id.page_1 -> {
                                    Log.d(TAG, "PAGE1")

                                }

                                R.id.page_2 -> {
                                    Log.d(TAG, "PAGE2")

                                }
                            }
                        }
                    }

                    "Magic" -> {
                        binding.bottomNavigation.inflateMenu(R.menu.bottom_navigation_menu_magic)
                        binding.bottomNavigation.setOnItemSelectedListener { item ->
                            when (item.itemId) {
                                R.id.page_1 -> {
//                            sharedViewModel.setItem(Items.WEAPON)
                                    true
                                }

                                R.id.page_2 -> {
//                            sharedViewModel.setItem(Items.ARMOR)
                                    true
                                }

                                else -> false
                            }
                        }

                        binding.bottomNavigation.setOnItemReselectedListener { item ->
                            when (item.itemId) {
                                R.id.page_1 -> {
//                            sharedViewModel.setItem(Items.WEAPON)
                                    Log.d(TAG, "PAGE1")
                                }

                                R.id.page_2 -> {
//                            sharedViewModel.setItem(Items.ARMOR)
                                    Log.d(TAG, "PAGE2")

                                }
                            }
                        }
                    }

                    else -> {

                        binding.statusRecyclerView.visibility = View.VISIBLE
                        binding.dividerStatus1.visibility = View.VISIBLE
                        binding.fabEdit.visibility = View.VISIBLE
                        binding.itemRecyclerView.visibility = View.GONE
                        binding.bottomNavigation.visibility = View.GONE

                        binding.fabEdit.setOnClickListener {
                            CustomizeBuildModalBottomSheet().show(
                                parentFragmentManager,
                                "CustomizeBottomSheet"
                            )
                        }
                    }
                }


            }
        }


        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}