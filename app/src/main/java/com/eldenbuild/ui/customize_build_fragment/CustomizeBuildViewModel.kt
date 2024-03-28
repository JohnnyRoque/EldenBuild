package com.eldenbuild.ui.customize_build_fragment

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.eldenbuild.data.database.CharacterStatus
import com.eldenbuild.data.database.ItemsDefaultCategories
import com.eldenbuild.data.repository.BuildRepository
import com.eldenbuild.data.repository.ItemRepository
import com.eldenbuild.ui.build_detail_fragment.BuildDetailViewModel
import com.eldenbuild.util.Items
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Criar variaveis para os diferentes tipos de items, e então usar um loop para retorna-los
// setItem vai informar qual dessas listas sera atribuido ao valor de curretnList
@OptIn(ExperimentalCoroutinesApi::class)
class CustomizeBuildViewModel(
    private val itemOnlineRepository: ItemRepository,
    private val buildRepository: BuildRepository,
    private val savedStateHandle: SavedStateHandle
) :
    ViewModel() {
    /**
     * Stream of immutable states representative of the UI.
     */
    val state: StateFlow<ItemListUiState>
    val pagingDataFlow: Flow<PagingData<ItemsDefaultCategories>>
    val accept :(UiAction) -> Unit


    init {
        val initialGroup : String = savedStateHandle[LAST_SEARCH_QUERY] ?: DEFAULT_GROUP
        val lastQueryScrolled: String = savedStateHandle[LAST_QUERY_SCROLLED] ?: DEFAULT_GROUP
        val actionStateFlow = MutableSharedFlow<UiAction>()
        val searches = actionStateFlow
            .filterIsInstance<UiAction.Search>()
            .distinctUntilChanged()
            .onStart { emit(UiAction.Search(initialGroup)) }
        val queriesScrolled = actionStateFlow
            .filterIsInstance<UiAction.Scroll>()
            .distinctUntilChanged()
            // This is shared to keep the flow "hot" while caching the last query scrolled,
            // otherwise each flatMapLatest invocation would lose the last query scrolled,
            .shareIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000L),
                replay = 1
            )
            .onStart { emit(UiAction.Scroll(lastQueryScrolled)) }
        pagingDataFlow = searches.flatMapLatest {
            getItemList(it.group) }
            .cachedIn(viewModelScope)

        state = combine(
            searches,
            queriesScrolled,
            ::Pair
        ).map { (search, scroll) ->
            ItemListUiState(
                group = search.group,
                lastQueryScrolled = scroll.currentGroup,
                hasNotScrolledForCurrentPosition = search.group != scroll.currentGroup

            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = ItemListUiState()
        )
        accept ={action ->
            viewModelScope.launch { actionStateFlow.emit(action) }
        }


    }

    private fun getItemList(group: String): Flow<PagingData<ItemsDefaultCategories>> =
        itemOnlineRepository.getStreamOfItems(group)

//    .filterNotNull()
//    .map { itemResponse ->
//        itemResponse.data.also { list ->
//            list.sortedBy { it.category }
//            list.map {
//                when (group) {
//                    Items.WEAPON -> it.itemType = Items.WEAPON
//                    Items.ARMOR -> it.itemType = Items.ARMOR
//                    Items.SHIELD -> it.itemType = Items.SHIELD
//                    Items.TALISMANS -> it.itemType = Items.TALISMANS
//                }
//            }
//        }
//    }

//    val listOfWeapons: StateFlow<List<ItemsDefaultCategories>> =
//        getItemList(Items.WEAPON).stateIn(
//            viewModelScope,
//            SharingStarted.WhileSubscribed(5000),
//            listOf()
//        )
//
//    val listOfArmors: StateFlow<List<ItemsDefaultCategories>> =
//        getItemList(Items.ARMOR).stateIn(
//            viewModelScope,
//            SharingStarted.WhileSubscribed(5000),
//            listOf()
//        )
//
//    val listOfShields: StateFlow<List<ItemsDefaultCategories>> =
//        getItemList(Items.SHIELD).stateIn(
//            viewModelScope,
//            SharingStarted.WhileSubscribed(5000),
//            listOf()
//        )
//    val listOfTalismans: StateFlow<List<ItemsDefaultCategories>> =
//        getItemList(Items.TALISMANS).stateIn(
//            viewModelScope,
//            SharingStarted.WhileSubscribed(5000),
//            listOf()
//        )

    val uiState: StateFlow<Boolean> = (
            savedStateHandle.getStateFlow(
                IS_FROM_BUILD_DETAIL, false
            ))

    val changeState: (Boolean) -> Unit = {
        savedStateHandle[IS_FROM_BUILD_DETAIL] = it
    }

    private val _currentList = MutableStateFlow<List<ItemsDefaultCategories>>(listOf())
    val currentList: StateFlow<List<ItemsDefaultCategories>> = _currentList
    fun showList(itemList: List<ItemsDefaultCategories>) {
        _currentList.value = itemList

    }




    private val _uiStateCharacterStatus: MutableStateFlow<List<CharacterStatus>> =
        MutableStateFlow(listOf())

    val uiStateCharacterStatus: StateFlow<List<CharacterStatus>> = _uiStateCharacterStatus
    private fun updateUiStateStatus(newStatusList: List<CharacterStatus>) {
        for (i in newStatusList) {
            _uiStateCharacterStatus.update {
                listOf(i)
            }
        }

    }

    fun setNewAttribute(
        statusList: MutableList<CharacterStatus>,
    ) {
        try {
            val updateBuild = checkNotNull(BuildDetailViewModel.buildDetail.value).copy(
                buildCharacterStatus = statusList
            )
            viewModelScope.launch(Dispatchers.IO) {
                buildRepository.updateBuild(updateBuild)
            }
            updateUiStateStatus(updateBuild.buildCharacterStatus)
        } catch (e: NullPointerException) {
            Log.d("UpdateBuildException", "$e")
        }
    }


    override fun onCleared() {
        savedStateHandle[LAST_SEARCH_QUERY] = state.value.group
        savedStateHandle[LAST_QUERY_SCROLLED] = state.value.group
        super.onCleared()
    }

}

sealed class UiAction {
    data class Search(val group: String) : UiAction()
    data class Scroll(val currentGroup: String) : UiAction()
}

data class ItemListUiState(
    val group: String = DEFAULT_GROUP,
    val lastQueryScrolled: String = LAST_QUERY_SCROLLED,
    val hasNotScrolledForCurrentPosition: Boolean = false,
)

private const val DEFAULT_GROUP = Items.WEAPON
private const val LAST_QUERY_SCROLLED = "last_query_scrolled"
private const val LAST_SEARCH_QUERY = "last_query_search"





