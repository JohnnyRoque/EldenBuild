package com.eldenbuild.util.paging_source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.eldenbuild.data.database.ItemsDefaultCategories
import com.eldenbuild.data.network.EldenBuildApiService
import com.eldenbuild.data.network.NETWORK_PAGE_LIMIT
import com.eldenbuild.util.Items
import java.io.IOException

private const val ITEM_STARTING_PAGE_INDEX = 0

class ItemPagingSource(
    private val eldenBuildApiService: EldenBuildApiService,
    private val group: String

) :
    PagingSource<Int, ItemsDefaultCategories>() {

    //The load() function will be called by the Paging library to asynchronously fetch more data
    // that will be displayed as the user scrolls the screen.*/
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ItemsDefaultCategories> {
        val position = params.key ?: ITEM_STARTING_PAGE_INDEX
        return try {
            val response = eldenBuildApiService.getItems(group, params.loadSize, position)
            val items: List<ItemsDefaultCategories> = response.data
            items.map {
                when (group) {
                    Items.WEAPON -> it.itemType = Items.WEAPON
                    Items.ARMOR -> it.itemType = Items.ARMOR
                    Items.SHIELD -> it.itemType = Items.SHIELD
                    Items.TALISMANS -> it.itemType = Items.TALISMANS
                }
            }
            val nextKey = if (items.isEmpty()) {
                null
            } else {
                // initial load size = 3 * NETWORK_PAGE_SIZE
                // ensure we're not requesting duplicating items, at the 2nd request
                position + (params.loadSize / NETWORK_PAGE_LIMIT)
            }
            LoadResult.Page(
                data = items,
                prevKey = if (position == ITEM_STARTING_PAGE_INDEX) null else position - 1,
                nextKey = nextKey
            )
        } catch (e: IOException) {
            return LoadResult.Error(e)
        }
    }
    // The refresh key is used for subsequent refresh calls to PagingSource.
    // load after the initial load

    override fun getRefreshKey(state: PagingState<Int, ItemsDefaultCategories>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            // We need to get the previous key (or next key if previous is null) of the page
            // that was closest to the most recently accessed index.
            // Anchor position is the most recently accessed index
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)

        }
    }
}