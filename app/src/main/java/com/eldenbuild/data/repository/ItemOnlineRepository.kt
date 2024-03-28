package com.eldenbuild.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.eldenbuild.data.database.ItemsDefaultCategories
import com.eldenbuild.data.network.EldenBuildApiService
import com.eldenbuild.data.network.NETWORK_PAGE_LIMIT
import com.eldenbuild.util.paging_source.ItemPagingSource
import kotlinx.coroutines.flow.Flow

class ItemOnlineRepository(private val eldenBuildApiService: EldenBuildApiService) :
    ItemRepository {
    override fun getStreamOfItems(group: String): Flow<PagingData<ItemsDefaultCategories>> {
        return Pager(
            config = PagingConfig(
                enablePlaceholders = false,
                pageSize = NETWORK_PAGE_LIMIT
            ),
            pagingSourceFactory = {ItemPagingSource(eldenBuildApiService,group)}
        ).flow
    }
}