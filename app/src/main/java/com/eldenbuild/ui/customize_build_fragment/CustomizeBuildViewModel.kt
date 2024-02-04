package com.eldenbuild.ui.customize_build_fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eldenbuild.data.database.ItemsDefaultCategories
import com.eldenbuild.data.repository.ItemRepository
import com.eldenbuild.util.Items
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

// Criar variaveis para os diferentes tipos de items, e então usar um loop para retorna-los
// setItem vai informar qual dessas listas sera atribuido ao valor de curretnList
class CustomizeBuildViewModel(private val itemOnlineRepository: ItemRepository) :
    ViewModel() {

    val currentListWeapons: StateFlow<List<ItemsDefaultCategories>> =
        getItemList(Items.WEAPON).stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            listOf()
        )

    val currentListArmors: StateFlow<List<ItemsDefaultCategories>> =
        getItemList(Items.ARMOR).stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            listOf()
        )

    val currentListShields: StateFlow<List<ItemsDefaultCategories>> =
        getItemList(Items.SHIELD).stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            listOf()
        )
    val currentListTalismans: StateFlow<List<ItemsDefaultCategories>> =
        getItemList(Items.TALISMANS).stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            listOf()
        )






    private fun getItemList(group: String): Flow<List<ItemsDefaultCategories>> {
        val limit = 50
        var page = 0

        return itemOnlineRepository.getStreamOfItems(group, limit, page).map { itemResponse ->
            itemResponse.data.also {list ->
                list.sortedBy { it.category }
            }
        }
    }
}




