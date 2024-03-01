package com.eldenbuild

import android.content.ClipData.Item
import com.eldenbuild.data.database.ItemsDefaultCategories
import com.eldenbuild.data.network.ItemResponse
import com.eldenbuild.data.repository.ItemOnlineRepository
import com.eldenbuild.util.Items
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(MockitoJUnitRunner::class)
class ExampleUnitTest {
    @Mock
    val fakeItemRepository: ItemOnlineRepository = Mockito.mock(ItemOnlineRepository::class.java)

    @Test
    fun streamOfItemsTest() = runTest {

        val fakeList = mutableListOf<ItemsDefaultCategories>()
        val emptyItem = ItemsDefaultCategories()

        repeat(50) {
            fakeList.add(emptyItem)
        }
        val itemResponse = ItemResponse(
            true,
            fakeList.size,
            100,
            fakeList
        )


        fun getItemList(group: String): Flow<List<ItemsDefaultCategories>> {
            val limit = 50
            var page = 0
            Mockito.`when`(fakeItemRepository.getStreamOfItems(group, limit, page)).thenReturn(
                flow {
                    emit(itemResponse)
                }
            )
            return fakeItemRepository.getStreamOfItems(group, limit, page)
                .filterNotNull()
                .map { itemResponse ->
                    itemResponse.data.also { list ->
                        list.sortedBy { it.category }
                        list.map {
                            when (group) {
                                Items.WEAPON -> it.itemType = Items.WEAPON
                                Items.ARMOR -> it.itemType = Items.ARMOR
                                Items.SHIELD -> it.itemType = Items.SHIELD
                                Items.TALISMANS -> it.itemType = Items.TALISMANS
                            }
                        }
                    }
                }
        }

        val list = getItemList(Items.WEAPON).last()
        assertEquals(Items.WEAPON, list.first().itemType)

    }

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

//    @Test
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


    @Test
    fun statsMap() {
        data class Item(
            val title: String,
        )

        val list = mutableListOf(Item("Hookclaws"))

        fun addNewItem(item: Item) {
            var sameItemCount = 1

            for (i in list) {
                if (i.title.substringBefore("(") == item.title && list.contains(item)) {
                    sameItemCount++
                }
            }
            when {
                sameItemCount > 1 -> {

                    var newItem = item.copy(
                        title = item.title + "($sameItemCount)"
                    )
                    if (list.contains(newItem)) {
                        newItem = item.copy(
                            title = item.title + "(${sameItemCount.dec()})"
                        )
                    }
                    list.add(newItem)
                }

                else -> {
                    list.add(item)
                }
            }
        }

        addNewItem(Item("Hookclaws"))
        addNewItem(Item("Hookclaws"))
        list.removeAt(1)
        addNewItem(Item("Sword"))
        addNewItem(Item("Sword"))
        addNewItem(Item("Hookclaws"))
        addNewItem(Item("Sword"))
        list.removeAt(0)
        addNewItem(Item("Hookclaws"))


        addNewItem(Item("Hookclaws"))


        assertEquals(
            listOf(Item("Hookclaws"), Item("Hookclaws(2)"), Item("Hookclaws(3)")),
            list
        )

    }
}


fun setNewAttribute(attribute: Int, isInc: Boolean): Int {
    val add: (Int) -> Int = { it + 1 }
    val sub: (Int) -> Int = { it - 1 }
    return when {
        !isInc && attribute > 0 -> sub(attribute)
        else -> add(attribute)
    }
}







