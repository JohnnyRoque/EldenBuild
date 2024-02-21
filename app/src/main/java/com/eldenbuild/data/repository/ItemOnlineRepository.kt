package com.eldenbuild.data.repository

import android.util.Log
import com.eldenbuild.data.network.EldenBuildApiService
import com.eldenbuild.data.network.ItemResponse
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive

class ItemOnlineRepository(private val eldenBuildApiService: EldenBuildApiService) :
    ItemRepository {

    override fun getStreamOfItems(group: String, limit: Int, page: Int): Flow<ItemResponse> = flow {

        while (currentCoroutineContext().isActive) {

            try {
                emit(eldenBuildApiService.getItems(group, limit, page))

            } catch (e: Exception) {
                Log.d("ItemResponse", "Empty response")
            }
        }
    }
}