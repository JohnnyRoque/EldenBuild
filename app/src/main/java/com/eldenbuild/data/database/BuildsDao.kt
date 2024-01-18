package com.eldenbuild.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface BuildsDao {
    @Insert
    suspend fun insertBuild(buildCategories: BuildCategories)

    @Update
    suspend fun updateBuild(buildCategories: BuildCategories)
    @Delete
    suspend fun deleteBuild(buildCategories: BuildCategories)

    @Query("SELECT * FROM build_categories")
    fun getAllBuilds(): Flow<List<BuildCategories>>
    @Query("SELECT * FROM build_categories WHERE buildId = :id")
    fun getBuildDetail(id:Int) : Flow<BuildCategories>

}