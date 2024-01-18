package com.eldenbuild.ui.item_detail_fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.eldenbuild.data.database.ItemsDefaultCategories

class ItemDetailViewModel : ViewModel() {

    private val _itemDetail = MutableLiveData<ItemsDefaultCategories>()
    val itemDetail: LiveData<ItemsDefaultCategories> = _itemDetail
}
