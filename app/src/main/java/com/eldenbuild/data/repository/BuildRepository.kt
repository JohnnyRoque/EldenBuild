package com.eldenbuild.data.repository

import com.eldenbuild.data.database.BuildCategories
import kotlinx.coroutines.flow.Flow

interface BuildRepository {
    fun getAllBuildStream(): Flow<List<BuildCategories>>
    fun getBuildStream(id: Int): Flow<BuildCategories>
    suspend fun addNewBuild(buildCategories: BuildCategories){}
    suspend fun updateBuild(buildCategories: BuildCategories){}
    suspend fun deleteBuild(buildCategories: BuildCategories){}

}