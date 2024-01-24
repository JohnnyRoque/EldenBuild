package com.eldenbuild.data.repository

import com.eldenbuild.data.database.BuildCategories
import com.eldenbuild.data.database.BuildsDao
import kotlinx.coroutines.flow.Flow

class OfflineBuildRepository(private val buildsDao: BuildsDao) : BuildRepository {

    override fun getAllBuildStream(): Flow<List<BuildCategories>> = buildsDao.getAllBuilds()
    override fun getBuildStream(id: Int): Flow<BuildCategories> = buildsDao.getBuildDetail(id)

    override suspend fun addNewBuild(buildCategories: BuildCategories) =
        buildsDao.insertBuild(buildCategories)

    override suspend fun updateBuild(buildCategories: BuildCategories) =
        buildsDao.updateBuild(buildCategories)

    override suspend fun deleteBuild(buildCategories: BuildCategories) =
        buildsDao.deleteBuild(buildCategories)

}


