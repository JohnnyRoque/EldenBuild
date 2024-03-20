package com.eldenbuild

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import com.eldenbuild.application.appModule
import com.eldenbuild.data.database.BuildCategories
import com.eldenbuild.data.database.ItemsDefaultCategories
import com.eldenbuild.data.network.ItemResponse
import com.eldenbuild.data.repository.ItemOnlineRepository
import com.eldenbuild.data.repository.ItemRepository
import com.eldenbuild.util.Items
import com.eldenbuild.viewmodel.OverViewViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.check.checkModules
import org.koin.test.inject
import org.koin.test.mock.MockProviderRule
import org.koin.test.mock.declareMock
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

@RunWith(MockitoJUnitRunner::class)
class TestDeleteBuild() : KoinTest {
    @get:Rule
    val mockProviderRule = MockProviderRule.create { clazz ->
        Mockito.mock(clazz.java)
    }

    @Mock
    private val context: Context = Mockito.mock(Context::class.java)

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        androidContext(context)
        printLogger()

        modules(appModule)
    }

    private val overViewViewModel: OverViewViewModel by inject<OverViewViewModel>()

    @Mock
    val build: BuildCategories = Mockito.mock(BuildCategories::class.java)


    @Test
    fun deleteBuild() = runTest {
        val dummy = build.copy(title = "dummy")
        overViewViewModel.createNewBuild("dummy", category = "test", description = null )
        val checkList = listOf<BuildCategories>(dummy)
        backgroundScope.launch {
        assertTrue(
            overViewViewModel.deleteCheckedBuild(checkList)
        )
        }
    }
}


class DiTest : KoinTest {
    @get:Rule
    val mockProvider = MockProviderRule.create { clazz ->
        Mockito.mock(clazz.java)
    }

    @Mock
    val context: Context = Mockito.mock(Context::class.java)

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        androidContext(context)
        printLogger()
        declareMock<SavedStateHandle> {
            this["IS_FROM_BUILD_DETAIL"] = true
        }
        checkModules {
            modules(appModule)
        }

    }
}



@RunWith(MockitoJUnitRunner::class)
class TestCheckedItems : KoinTest {
    @Mock
    val buildCategories: BuildCategories = Mockito.mock(BuildCategories::class.java)

    @get:Rule
    val mockProvider = MockProviderRule.create { clazz ->
        Mockito.mock(clazz)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testCheckItemsListFlow() = runTest {
        val _stateTest: MutableStateFlow<MutableList<String>> =
            MutableStateFlow(mutableListOf())
        val stateTest: StateFlow<MutableList<String>> = _stateTest

        fun addString(s: String) {
            if (!_stateTest.value.contains(s)) {
                _stateTest.value.add(s)
            }
        }

        fun removeString(string: String) {
            if (_stateTest.value.contains(string)) {
                _stateTest.value.remove(string)
            }
        }

        addString("g")
        addString("c")
        addString("t")
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            addString("2")
            stateTest.collect() {}

        }
        addString("k")
        Assert.assertTrue(stateTest.value.contains("k") && stateTest.value.contains("2"))

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun deleteItemsFromBuild() {
        val list1 = mutableListOf<String>(
            "John",
            "Jane",
            "Max",
            "Sarah",
            "Cloe",
            "John",
            "Saint",
            "Carl",
            "Connor"
        )
        val checkedItems = mutableListOf("Sarah", "Cloe", "Connor", "Saint", "Carl", "John")
        list1.removeAll(checkedItems)
        assertTrue(list1 != checkedItems)
    }

    @RunWith(MockitoJUnitRunner::class)
    class ExampleUnitTest : KoinTest {

        private val testRepositoryModule = module {
            single<ItemRepository> {
                Mockito.mock(ItemOnlineRepository::class.java)
            }
        }

        @get:Rule
        val koinTestRule = KoinTestRule.create {
            modules(testRepositoryModule)
        }
        private val fakeItemRepository: ItemRepository by inject()

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
            assertEquals(50, list.size)

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
}








