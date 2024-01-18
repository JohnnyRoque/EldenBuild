package com.eldenbuild

import com.eldenbuild.data.database.CharacterStatus
import com.eldenbuild.data.database.ItemsDefaultCategories
import com.eldenbuild.data.network.EldenBuildApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun sortList() {
        var list1: List<ItemsDefaultCategories>
        var list2: List<ItemsDefaultCategories>

        runBlocking {
            list1 = EldenBuildApi.retrofitService.getArmors().data
            list2 = EldenBuildApi.retrofitService.getArmors().data
        }
        val peopleNames = listOf("Fred", "Ann", "Barbara", "Joe")
        println(peopleNames.sorted())
        list1.sortedBy { it.category }

        assertEquals("Resultado listas $list1 and $list2", list1, list2)

    }

    @Test
    fun statsMap() {
        val newCharacterStatusList = listOf(
            CharacterStatus("Vigor", 0,),
            CharacterStatus("Mind", 0,),
            CharacterStatus("Endurance", 0,),
            CharacterStatus("Strength", 0,),
            CharacterStatus("Dexterity", 0,),
            CharacterStatus("Intelligence", 0,),
            CharacterStatus("Faith", 0,),
            CharacterStatus("Arcane", 0,),
        )
    }
    fun setNewAttribute(attribute: Int,isInc:Boolean): Int {
        val add: (Int) -> Int = { it + 1 }
        val sub: (Int) -> Int = { it - 1 }
        return when{
            !isInc && attribute > 0 -> sub(attribute)
        else -> add(attribute)
        }
    }
}