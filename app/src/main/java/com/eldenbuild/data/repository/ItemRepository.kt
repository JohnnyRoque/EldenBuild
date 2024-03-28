package com.eldenbuild.data.repository

import androidx.paging.PagingData
import com.eldenbuild.data.database.ItemsDefaultCategories
import kotlinx.coroutines.flow.Flow

interface ItemRepository {
    fun getStreamOfItems(group: String): Flow<PagingData<ItemsDefaultCategories>>

}