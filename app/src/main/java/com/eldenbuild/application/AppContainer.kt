package com.eldenbuild.application

import android.content.Context
import com.eldenbuild.data.database.AppDatabase
import com.eldenbuild.data.repository.BuildRepository
import com.eldenbuild.data.repository.OfflineBuildRepository

/**
 * App container for Dependency injection.
 */
interface AppContainer{
    val offlineBuildRepository : BuildRepository
}

class AppDataContainer(private val context: Context) : AppContainer{

    override val offlineBuildRepository: BuildRepository by lazy {
        OfflineBuildRepository(AppDatabase.getDatabase(context).buildsDao())
    }
}

