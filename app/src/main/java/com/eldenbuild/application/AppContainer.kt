package com.eldenbuild.application

import android.content.Context
import com.eldenbuild.data.database.AppDatabase
import com.eldenbuild.data.network.EldenBuildApi
import com.eldenbuild.data.repository.BuildRepository
import com.eldenbuild.data.repository.ItemOnlineRepository
import com.eldenbuild.data.repository.ItemRepository
import com.eldenbuild.data.repository.OfflineBuildRepository

/**
 * App container for Dependency injection.
 */
interface AppContainer {

    val offlineBuildRepository: BuildRepository
    val onlineItemRepository: ItemRepository

}
class AppDataContainer(private val context: Context) : AppContainer {

    override val offlineBuildRepository: BuildRepository by lazy {
        OfflineBuildRepository(AppDatabase.getDatabase(context).buildsDao())
    }

    override val onlineItemRepository: ItemRepository by lazy {
        ItemOnlineRepository(EldenBuildApi.retrofitService)
    }

}

