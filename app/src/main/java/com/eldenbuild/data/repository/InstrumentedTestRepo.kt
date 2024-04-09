package com.eldenbuild.data.repository

import com.eldenbuild.data.database.BuildCategories
import com.eldenbuild.data.database.ItemsDefaultCategories
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.UUID

class InstrumentedTestRepo :
    BuildRepository {

    private val itemList = mutableListOf<ItemsDefaultCategories>()

    private val dummyBuild = BuildCategories (
        title = "Dwight",
        category = "",
        description = "",
        buildId = 0,
        buildItems = mutableListOf(),
        buildCharacterStatus = mutableListOf()
    )
    private val dummyList: MutableList<BuildCategories> = mutableListOf()
    override fun getAllBuildStream(): Flow<List<BuildCategories>> = flow {
        var idCount = 0
        repeat(5) {
            itemList.add(ItemsDefaultCategories(name = "dummy", id = UUID.randomUUID().toString() ))
        }
        repeat(5) {
            dummyList.add(dummyBuild.copy(
                title = "Dwight$idCount",
                buildId = idCount,
                buildItems = itemList
            ))
            idCount += 1
        }
        emit(dummyList)
    }
    override fun getBuildStream(id: Int): Flow<BuildCategories> = flow {
        for (i in dummyList) {
            if (i.buildId == id) {
                emit(i)
            }
        }
    }
}
