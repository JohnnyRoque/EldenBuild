package com.eldenbuild.ui.item_detail_fragment

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eldenbuild.data.database.ItemsDefaultCategories
import com.eldenbuild.data.repository.BuildRepository
import com.eldenbuild.data.repository.ItemRepository
import com.eldenbuild.ui.build_detail_fragment.BuildDetailViewModel.CurrentBuild.buildDetail
import com.eldenbuild.ui.builds_overview_fragment.TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ItemDetailViewModel(
    private val itemOnlineRepository: ItemRepository,
    private val offlineBuildRepository: BuildRepository
) : ViewModel() {

    private fun addNewItem(
        item: ItemsDefaultCategories,
        list: List<ItemsDefaultCategories>
    ): ItemsDefaultCategories {
        var sameItemCount = 1

        for (i in list) {
            if (i.name.substringBefore("(") == item.name && list.contains(item)) {
                sameItemCount++
            }
        }
        return when {
            sameItemCount > 1 -> {

                var newItem = item.copy(
                    name = item.name + "($sameItemCount)"
                )
                if (list.contains(newItem)) {
                    newItem = item.copy(
                        name = item.name + "(${sameItemCount.dec()})"
                    )
                }
                newItem
            }

            else -> {
                item
            }
        }
    }


    val itemDetail: StateFlow<ItemsDefaultCategories> = _itemDetail

    fun addItemToBuild(item: ItemsDefaultCategories): String {
        var message: String

        try {

            val newUpdatedBuild = checkNotNull(buildDetail.value).copy()

            newUpdatedBuild.buildItems.add(addNewItem(item, newUpdatedBuild.buildItems))

            newUpdatedBuild.buildItems.map {
                it.fromBuild = newUpdatedBuild.title
            }


            viewModelScope.launch(Dispatchers.IO) {

                offlineBuildRepository.updateBuild(newUpdatedBuild)

            }

            message = "Item added to build"

        } catch (e: IllegalStateException) {
            message = "Failed to add an item"
            Log.d(TAG, "$e")
        }
        return message
    }

    companion object {

        private val _itemDetail = MutableStateFlow(ItemsDefaultCategories())
        fun getCurrentItem(itemsDefaultCategories: ItemsDefaultCategories) =
            try {
                _itemDetail.value = itemsDefaultCategories
            } catch (e: Exception) {
                Log.d("GetCurrentItem", "$e")
            }

    }
}
