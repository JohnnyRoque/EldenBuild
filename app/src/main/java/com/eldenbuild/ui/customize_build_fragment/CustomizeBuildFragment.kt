package com.eldenbuild.ui.customize_build_fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.eldenbuild.R
import com.eldenbuild.databinding.FragmentCustomizeBuildBinding
import com.eldenbuild.ui.build_detail_fragment.BuildItemsGridAdapter
import com.eldenbuild.ui.builds_overview_fragment.TAG
import com.eldenbuild.util.Items
import com.eldenbuild.viewmodel.OverViewViewModel

const val CUSTOMIZE_LABEL = "customizeLabel"

class CustomizeBuildFragment : Fragment() {
    private var argumentLabel: String? = null
    private var _binding: FragmentCustomizeBuildBinding? = null
    val binding get() = _binding!!
    private val sharedViewModel: OverViewViewModel by activityViewModels()

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

        binding.itemRecyclerView.adapter = BuildItemsGridAdapter() {
            sharedViewModel.showItemDetail(it)
            findNavController().navigate(
                CustomizeBuildFragmentDirections.actionCustomizeBuildFragmentToItemDetailsFragment()
            )
        }
        binding.apply {
            viewModel = sharedViewModel
            label = argumentLabel
            lifecycleOwner = viewLifecycleOwner

        }

        when (argumentLabel) {
            "Equipment" -> {
                binding.bottomNavigation.inflateMenu(R.menu.bottom_navigation_menu)
                binding.bottomNavigation.setOnItemSelectedListener { item ->
                    when (item.itemId) {
                        R.id.page_1 -> {
                            sharedViewModel.setItem(Items.WEAPON)
                            Log.d(TAG, "${sharedViewModel.showItemList.value}")
                            true
                        }

                        R.id.page_2 -> {
                            sharedViewModel.setItem(Items.ARMOR)
                            Log.d(TAG, "${sharedViewModel.showItemList.value}")
                            true
                        }

                        else -> false
                    }
                }
                binding.bottomNavigation.setOnItemReselectedListener { item ->
                    when (item.itemId) {
                        R.id.page_1 -> {
                            sharedViewModel.setItem(Items.WEAPON)
                            Log.d(TAG, "PAGE1")

                        }

                        R.id.page_2 -> {
                            sharedViewModel.setItem(Items.ARMOR)
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
                            sharedViewModel.setItem(Items.WEAPON)
                            Log.d(TAG, "${sharedViewModel.showItemList.value}")
                            true
                        }

                        R.id.page_2 -> {
                            sharedViewModel.setItem(Items.ARMOR)
                            Log.d(TAG, "${sharedViewModel.showItemList.value}")
                            true
                        }

                        else -> false
                    }
                }
                binding.bottomNavigation.setOnItemReselectedListener { item ->
                    when (item.itemId) {
                        R.id.page_1 -> {
                            sharedViewModel.setItem(Items.WEAPON)
                            Log.d(TAG, "PAGE1")

                        }

                        R.id.page_2 -> {
                            sharedViewModel.setItem(Items.ARMOR)
                            Log.d(TAG, "PAGE2")

                        }
                    }
                }
            }
            else -> {
                binding.itemRecyclerView.visibility = View.INVISIBLE
                binding.bottomNavigation.visibility = View.INVISIBLE
            }
        }

        super.onViewCreated(view, savedInstanceState)
    }


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}