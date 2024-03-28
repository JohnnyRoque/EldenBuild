package com.eldenbuild.ui.customize_build_fragment

import android.os.Bundle
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
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.RecyclerView
import com.eldenbuild.R
import com.eldenbuild.data.database.ItemsDefaultCategories
import com.eldenbuild.databinding.FragmentCustomizeBuildBinding
import com.eldenbuild.ui.build_detail_fragment.BuildDetailViewModel
import com.eldenbuild.ui.item_detail_fragment.ItemDetailViewModel
import com.eldenbuild.util.Items
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.map
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



        val statusAdapter = BuildStatusAdapter(
            isEditAttributes = false,
            setNewLevel = {}
        )

        binding.apply {
            viewModel = sharedViewModel
            label = argumentLabel
            lifecycleOwner = viewLifecycleOwner
            currentBuild = BuildDetailViewModel.buildDetail.value
            statusRecyclerView.adapter = statusAdapter
            binding.bottomNavigation.selectedItemId = R.id.page_1
            bindState(sharedViewModel.state,sharedViewModel.pagingDataFlow,sharedViewModel.accept)
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
                binding.itemRecyclerView.visibility = View.VISIBLE
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

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun FragmentCustomizeBuildBinding.bindState(
        itemListUiState: StateFlow<ItemListUiState>,
        pagingData: Flow<PagingData<ItemsDefaultCategories>>,
        uiAction: (UiAction) -> Unit
    ) {
        val itemsGridAdapter = ItemsGridAdapter { item, itemType ->
            ItemDetailViewModel.getCurrentItem(item)
            findNavController().navigate(
                CustomizeBuildFragmentDirections.actionCustomizeBuildFragmentToItemDetailsFragment(
                    itemType,
                    isItemFromBuild = false
                )
            )
        }
        itemRecyclerView.adapter = itemsGridAdapter

       bottomSheetNavigate(uiAction)
        bindList(
            itemsGridAdapter,
            itemListUiState,
            pagingData,
            uiAction
        )

    }


    private fun FragmentCustomizeBuildBinding.bottomSheetNavigate(
        onQueryChanged: (UiAction.Search) -> Unit
    ) {

        bottomNavigation.setOnItemSelectedListener { item ->
            var title = ""
            when (argumentLabel) {
                "Equipment" -> {
                    when (item.itemId) {
                        R.id.page_1 -> title = Items.WEAPON
                        R.id.page_2 -> title = Items.ARMOR
                        R.id.page_3 -> title = Items.SHIELD
                        R.id.page_4 -> title = Items.TALISMANS
                    }
                }

                "Magic" -> {
                    when (item.itemId) {
                        R.id.page_1 -> title = Items.WEAPON
                        R.id.page_2 -> title = Items.ARMOR
                        R.id.page_3 -> title = Items.SHIELD
                        R.id.page_4 -> title = Items.TALISMANS
                    }
                }
            }
            updateItemListFromInput(title, onQueryChanged)
            true

        }

    }



    private fun FragmentCustomizeBuildBinding.updateItemListFromInput(
        menuItemTitle: String,
        onQueryChanged: (UiAction.Search) -> Unit
    ) {

        menuItemTitle.let { title ->
            if (title.isNotEmpty()) {
                itemRecyclerView.scrollToPosition(0)
                onQueryChanged(UiAction.Search(title))
            }
        }
    }

    private fun FragmentCustomizeBuildBinding.bindList(
        itemsGridAdapter: ItemsGridAdapter,
        uiState: StateFlow<ItemListUiState>,
        pagingData: Flow<PagingData<ItemsDefaultCategories>>,
        onScrollChanged: (UiAction.Scroll) -> Unit
    ) {
        itemRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy != 0) onScrollChanged(UiAction.Scroll(currentGroup = uiState.value.group))
                super.onScrolled(recyclerView, dx, dy)
            }

        })
        val notLoading = itemsGridAdapter.loadStateFlow
            // Only emit when REFRESH LoadState for RemoteMediator changes.

            .distinctUntilChangedBy { it.source.refresh }
            // Only react to cases where Remote REFRESH completes i.e., NotLoading.

            .map { it.source.refresh is LoadState.NotLoading }
        val hasNotScrolledForCurrentSearch = uiState.map {
            it.hasNotScrolledForCurrentPosition
        }.distinctUntilChanged()

        val shouldScrollTop = combine(
            notLoading,
            hasNotScrolledForCurrentSearch,
            Boolean::and
        )
            .distinctUntilChanged()

        lifecycleScope.launch {
            pagingData.collectLatest(itemsGridAdapter::submitData)
        }

        lifecycleScope.launch {
            shouldScrollTop.collect { scrollTop ->
                if (scrollTop) itemRecyclerView.scrollToPosition(0)
            }
        }

    }
}

