package com.eldenbuild

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
        data class Item (
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
                sameItemCount > 1-> {

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







