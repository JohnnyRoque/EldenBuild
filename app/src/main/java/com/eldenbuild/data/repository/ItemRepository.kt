package com.eldenbuild.data.repository

import com.eldenbuild.data.network.ItemResponse
import kotlinx.coroutines.flow.Flow

interface ItemRepository {
    fun getStreamOfItems(group: String, limit:Int, page: Int): Flow<ItemResponse>

}