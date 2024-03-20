package com.eldenbuild.ui.customize_build_fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.activity.addCallback
import androidx.activity.findViewTreeOnBackPressedDispatcherOwner
import androidx.activity.setViewTreeOnBackPressedDispatcherOwner
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.eldenbuild.R
import com.eldenbuild.databinding.FragmentCustomizeBuildBinding
import com.eldenbuild.ui.build_detail_fragment.BuildDetailViewModel
import com.eldenbuild.ui.build_detail_fragment.BuildItemsGridAdapter
import com.eldenbuild.ui.builds_overview_fragment.TAG
import com.eldenbuild.ui.item_detail_fragment.ItemDetailViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

const val CUSTOMIZE_LABEL = "customizeLabel"
const val IS_FROM_BUILD_DETAIL = "isFromBuildDetail"

//Agora o flow esta emitindo valores, antes não estava devido a ao novo valor CharacterStatus ser
//igual mesmo mundo no level. Solução foi fazer um for e chamar update e listOf(i)
class CustomizeBuildFragment : Fragment() {
    private var argumentLabel: String? = null
    private var _binding: FragmentCustomizeBuildBinding? = null
    val binding get() = _binding!!
    private val sharedViewModel: CustomizeBuildViewModel by viewModel<CustomizeBuildViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            argumentLabel = it.getString(CUSTOMIZE_LABEL)

        }
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

        val itemAdapter = BuildItemsGridAdapter(
            isItemSelectable = false,
            checkedItems = { _, _, _ -> }) { item, itemType ->
            ItemDetailViewModel.getCurrentItem(item)
            findNavController().navigate(
                CustomizeBuildFragmentDirections.actionCustomizeBuildFragmentToItemDetailsFragment(
                    type = itemType
                )
            )
        }

        val statusAdapter = BuildStatusAdapter(
            isEditAttributes = false,
            setNewLevel = {}
        )

        binding.apply {
            viewModel = sharedViewModel
            label = argumentLabel
            lifecycleOwner = viewLifecycleOwner
            currentBuild = BuildDetailViewModel.buildDetail.value
            itemRecyclerView.adapter = itemAdapter
            statusRecyclerView.adapter = statusAdapter
        }

        binding.fabEdit.setOnClickListener {
            CustomizeBuildModalBottomSheet(confirmNewAttributes = { statusList ->
                sharedViewModel.setNewAttribute(statusList)
            })
                .show(
                    requireActivity().supportFragmentManager,
                    "CustomizeBottomSheet"
                )
        }

        when (argumentLabel) {

            "Equipment" -> {

                binding.bottomNavigation.inflateMenu(R.menu.bottom_navigation_menu)
                binding.statusRecyclerView.visibility = View.GONE
                binding.dividerStatus1.visibility = View.GONE
                binding.fabEdit.visibility = View.GONE

            }

            "Magic" -> {

                binding.bottomNavigation.inflateMenu(R.menu.bottom_navigation_menu_magic)
                binding.statusRecyclerView.visibility = View.GONE
                binding.dividerStatus1.visibility = View.GONE
                binding.fabEdit.visibility = View.GONE

            }

            else -> {

                binding.statusRecyclerView.visibility = View.VISIBLE
                binding.dividerStatus1.visibility = View.VISIBLE
                binding.fabEdit.visibility = View.VISIBLE
                binding.itemRecyclerView.visibility = View.GONE
                binding.bottomNavigation.visibility = View.GONE

            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {


                launch {
                   sharedViewModel.uiStateCharacterStatus.collect{
                       statusAdapter.notifyDataSetChanged()
                       Log.d("stateUiStatus","Collect")
                       Log.d("stateUiStatus","$it")

                   }
                }

                when (argumentLabel) {

                    "Equipment" -> {

                        launch {
                            sharedViewModel.uiState.collect { uiState ->
                                if (uiState) {
                                    launch {
                                        sharedViewModel.listOfWeapons.collect { weaponsList ->
                                            sharedViewModel.showList(weaponsList)
                                        }
                                    }
                                }
                                sharedViewModel.changeState(false)
                            }
                        }

                        launch {

                            sharedViewModel.currentList.collect { list ->
                                if (list.isEmpty()) {
                                    binding.loadingItemsAnimation.visibility = View.VISIBLE

                                } else {
                                    itemAdapter.submitList(list)
                                    binding.loadingItemsAnimation.visibility = View.GONE
                                    binding.itemRecyclerView.visibility = View.VISIBLE

                                }
                            }
                        }
                        binding.bottomNavigation.setViewTreeOnBackPressedDispatcherOwner(object :
                            OnBackPressedDispatcherOwner {
                            override val lifecycle: Lifecycle
                                get() = viewLifecycleOwner.lifecycle
                            override val onBackPressedDispatcher: OnBackPressedDispatcher
                                get() = requireActivity().onBackPressedDispatcher

                        })

                        binding.bottomNavigation.findViewTreeOnBackPressedDispatcherOwner()?.let {
                            it.onBackPressedDispatcher.addCallback(this@CustomizeBuildFragment) {
                                binding.bottomNavigation.selectedItemId = R.id.page_1
                                findNavController().navigateUp()
                            }
                        }

                        binding.bottomNavigation.setOnItemSelectedListener { item ->

                            when (item.itemId) {
                                R.id.page_1 -> {

                                    launch(Dispatchers.IO) {
                                        sharedViewModel.listOfWeapons.collect { list ->
                                            sharedViewModel.showList(list)
                                        }
                                    }
                                    Log.d(TAG, "PAGE1")
                                    true
                                }

                                R.id.page_2 -> {

                                    launch(Dispatchers.IO) {
                                        sharedViewModel.listOfArmors.collect {
                                            sharedViewModel.showList(it)

                                        }
                                    }
                                    Log.d(TAG, "PAGE2")
                                    true
                                }

                                R.id.page_3 -> {

                                    launch(Dispatchers.IO) {
                                        sharedViewModel.listOfShields.collect {
                                            sharedViewModel.showList(it)
                                        }
                                    }
                                    true
                                }

                                R.id.page_4 -> {

                                    launch(Dispatchers.IO) {

                                        sharedViewModel.listOfTalismans.collect {
                                            sharedViewModel.showList(it)
                                        }
                                    }
                                    true
                                }

                                else -> false
                            }
                        }
                    }

                    "Magic" -> {

                        launch {
                            sharedViewModel.currentList.collect {

                                if (it.isEmpty()) {
                                    binding.loadingItemsAnimation.visibility = View.VISIBLE

                                } else {
                                    itemAdapter.submitList(it)
                                    binding.loadingItemsAnimation.visibility = View.GONE
                                    binding.itemRecyclerView.visibility = View.VISIBLE
                                }
                            }
                        }

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