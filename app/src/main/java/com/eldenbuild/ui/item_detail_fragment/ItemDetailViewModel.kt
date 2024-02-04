package com.eldenbuild.ui.item_detail_fragment

import android.util.Log
import androidx.lifecycle.ViewModel
import com.eldenbuild.data.database.ItemsDefaultCategories
import com.eldenbuild.data.repository.BuildRepository
import com.eldenbuild.data.repository.ItemRepository
import com.eldenbuild.ui.builds_overview_fragment.TAG
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ItemDetailViewModel(
    itemOnlineRepository: ItemRepository,
    offlineBuildRepository: BuildRepository
) : ViewModel() {

    val itemDetail: StateFlow<ItemsDefaultCategories> = _itemDetail


    fun addItemToBuild(item: ItemsDefaultCategories): String {
        var message: String
        try {
            message = "Item added to build"

        } catch (e: Exception) {
            message = "Failure"
            Log.d(TAG, "$e")
        }
        return message
    }


    companion object{
        private val _itemDetail = MutableStateFlow(ItemsDefaultCategories())
        fun getCurrentItem(itemsDefaultCategories: ItemsDefaultCategories) =
             try {
                 _itemDetail.value = itemsDefaultCategories
            } catch (e:Exception){
                Log.d("GetCurrentItem", "$e")
            }

    }
}
