package com.eldenbuild.ui.customize_build_fragment

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eldenbuild.data.database.ItemsDefaultCategories
import com.eldenbuild.data.repository.ItemRepository
import com.eldenbuild.util.Items
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

// Criar variaveis para os diferentes tipos de items, e então usar um loop para retorna-los
// setItem vai informar qual dessas listas sera atribuido ao valor de curretnList
class CustomizeBuildViewModel(
    private val itemOnlineRepository: ItemRepository,
    private val savedStateHandle: SavedStateHandle
) :
    ViewModel() {


    val listOfWeapons: StateFlow<List<ItemsDefaultCategories>> =
        getItemList(Items.WEAPON).stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            listOf()
        )

    val listOfArmors: StateFlow<List<ItemsDefaultCategories>> =
        getItemList(Items.ARMOR).stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            listOf()
        )

    val listOfShields: StateFlow<List<ItemsDefaultCategories>> =
        getItemList(Items.SHIELD).stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            listOf()
        )
    val listOfTalismans: StateFlow<List<ItemsDefaultCategories>> =
        getItemList(Items.TALISMANS).stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            listOf()
        )

    val uiState: StateFlow<Boolean> = (
        savedStateHandle.getStateFlow (
            IS_FROM_BUILD_DETAIL, false
        )
    )

    val changeState: (Boolean) -> Unit = {
        savedStateHandle[IS_FROM_BUILD_DETAIL] = it
    }

    private val _currentList = MutableStateFlow<List<ItemsDefaultCategories>>(listOf())
    val currentList: StateFlow<List<ItemsDefaultCategories>> = _currentList
    fun showList(itemList: List<ItemsDefaultCategories>) {
        _currentList.value = itemList

    }

    private fun getItemList(group: String): Flow<List<ItemsDefaultCategories>> {
        val limit = 50
        var page = 0

        return itemOnlineRepository.getStreamOfItems(group, limit, page)
            .filterNotNull()
            .map { itemResponse ->
                itemResponse.data.also { list ->
                    list.sortedBy { it.category }
                    list.map {
                        when (group) {
                            Items.WEAPON -> it.itemType = Items.WEAPON
                            Items.ARMOR -> it.itemType = Items.ARMOR
                            Items.SHIELD -> it.itemType = Items.SHIELD
                            Items.TALISMANS -> it.itemType = Items.TALISMANS
                        }
                    }
                }
            }
    }
}




