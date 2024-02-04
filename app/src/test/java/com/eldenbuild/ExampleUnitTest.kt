package com.eldenbuild

import com.eldenbuild.data.database.CharacterStatus
import com.eldenbuild.data.database.ItemsDefaultCategories
import com.eldenbuild.data.network.EldenBuildApi
import com.eldenbuild.data.network.ItemResponse
import com.eldenbuild.data.repository.ItemRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(JUnit4::class)
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
//    fun sortList() {
//        var list1: List<ItemsDefaultCategories>
//        var list2: List<ItemsDefaultCategories>
////
////        runBlocking {
////            list1 = EldenBuildApi.retrofitService.getArmors().data
////            list2 = EldenBuildApi.retrofitService.getArmors().data
////        }
//        val peopleNames = listOf("Fred", "Ann", "Barbara", "Joe")
//        println(peopleNames.sorted())
//        list1.sortedBy { it.category }
//
//        assertEquals("Resultado listas $list1 and $list2", list1, list2)
//
  }

    @Test
    fun statsMap() {
        val newCharacterStatusList = listOf(
            CharacterStatus("Vigor", 0),
            CharacterStatus("Mind", 0),
            CharacterStatus("Endurance", 0),
            CharacterStatus("Strength", 0),
            CharacterStatus("Dexterity", 0),
            CharacterStatus("Intelligence", 0),
            CharacterStatus("Faith", 0),
            CharacterStatus("Arcane", 0),
        )
    }

    fun setNewAttribute(attribute: Int, isInc: Boolean): Int {
        val add: (Int) -> Int = { it + 1 }
        val sub: (Int) -> Int = { it - 1 }
        return when {
            !isInc && attribute > 0 -> sub(attribute)
            else -> add(attribute)
        }
    }

    @Test
    fun testItemList() {

        val _currentList: MutableStateFlow<List<ItemsDefaultCategories>> = MutableStateFlow(listOf())
        val currentList: StateFlow<List<ItemsDefaultCategories>> = _currentList

        fun getItemList(name: String) {
            var count: Int = 0
            var page: Int = 0
            MyFakeRepository().getStreamOfItems(name, limit = count, page).map {
                _currentList.value = it.data
                while (count < it.total) {
                    count = 100
                    page++
                }
            }
        }

        getItemList("weapon")
        assertEquals(100, currentList.value.size)

    }


}

class MyFakeRepository : ItemRepository {
    override fun getStreamOfItems(group: String, limit: Int, page: Int): Flow<ItemResponse> = flow {

        emit(object :ItemResponse{
            override val success: Boolean
                get() = true
            override val count: Int
                get() = 100
            override val total: Int
                get() = 356
            override val data: List<ItemsDefaultCategories>
                get() = mutableListOf()
        })
    }



}


