package com.eldenbuild.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.eldenbuild.data.database.BuildCategories
import com.eldenbuild.data.database.CharacterStatus
import com.eldenbuild.data.database.ItemArmors
import com.eldenbuild.data.database.ItemWeapons
import com.eldenbuild.data.database.ItemsDefaultCategories
import com.eldenbuild.data.network.EldenBuildApi
import com.eldenbuild.data.repository.BuildRepository
import com.eldenbuild.ui.builds_overview_fragment.TAG
import com.eldenbuild.util.Items
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlin.system.measureTimeMillis

const val NETWORK_TEST = "network_test"

class OverViewViewModel(private val buildRepository: BuildRepository) : ViewModel() {

    //OverViewViewModel
    val buildsList: LiveData<List<BuildCategories>> =
        buildRepository.getAllBuildStream().asLiveData()

    //BuildDetailFragment
    private val _currentBuild = MutableLiveData<BuildCategories>()
    val currentBuild: LiveData<BuildCategories> = _currentBuild

    //ItemDetailFragment
    private val _itemDetail = MutableLiveData<ItemsDefaultCategories>()
    val itemDetail: LiveData<ItemsDefaultCategories> = _itemDetail

    //CustomizeBuildFragment
    private val _showItemList = MutableLiveData<List<ItemsDefaultCategories>>()
    val showItemList: LiveData<List<ItemsDefaultCategories>> = _showItemList.map { list ->
        list.sortedBy { it.category }
    }

    private val _listOfWeapons: MutableLiveData<List<ItemWeapons>> = MutableLiveData()
    private val listOfWeapons: LiveData<List<ItemWeapons>> = _listOfWeapons

    private val _listOfArmors: MutableLiveData<List<ItemArmors>> = MutableLiveData()
    private val listOfArmors: LiveData<List<ItemArmors>> = _listOfArmors

    //CustomizeBuildFragment

    fun setNewAttribute(attributeName: String, isInc: Boolean) {
        val currentBuildStatus = _currentBuild.value!!
        currentBuildStatus.let {
            for (i in (0..it.buildCharacterStatus.lastIndex)) {
                val isGreaterThenZero = it.buildCharacterStatus[i].attributeLevel > 0
                val isLessThanNinetyNine = it.buildCharacterStatus[i].attributeLevel < 99
                if (attributeName == it.buildCharacterStatus[i].attributeName) {
                    when {
                        isInc && isLessThanNinetyNine -> {
                            it.buildCharacterStatus[i].attributeLevel++
                        }

                        !isInc && isGreaterThenZero -> {
                            it.buildCharacterStatus[i].attributeLevel--
                        }
                    }
                }
            }
        }
        _currentBuild.postValue(currentBuildStatus)
    }
    //CustomizeBuildFragment

    fun showItemDetail(itemId: String) {
        _showItemList.value?.let {
            for (i in 0..it.lastIndex) {
                val itemPosition = it[i]
                if (itemId == itemPosition.id) {
                    _itemDetail.value = itemPosition
                    Log.d(TAG, "${_itemDetail.value}")
                }
            }
        }
    }
    //BuildsOverviewFragment.kt

    fun createNewBuild(title: String, category: String, description: String?) {
        val newItemList = mutableListOf<ItemsDefaultCategories>()
        val newCharacterStatusList = mutableListOf(
            CharacterStatus("Vigor", 0),
            CharacterStatus("Mind", 0),
            CharacterStatus("Endurance", 0),
            CharacterStatus("Strength", 0),
            CharacterStatus("Dexterity", 0),
            CharacterStatus("Intelligence", 0),
            CharacterStatus("Faith", 0),
            CharacterStatus("Arcane", 0),
        )

        viewModelScope.launch(Dispatchers.IO) {
            try {
                buildRepository.addNewBuild(
                    BuildCategories(
                        title = title,
                        category = category,
                        description = description,
                        buildItems = newItemList,
                        buildCharacterStatus = newCharacterStatusList
                    )
                )
            } catch (e: Exception) {
                Log.d("TAG", "$e")
            }

        }
    }
//OverViewViewModel

     fun showBuildDetail(buildId: Int): LiveData<BuildCategories> =
        buildRepository.getBuildStream(buildId).asLiveData()


    //OverViewViewModel

    fun getBuildList(): Flow<List<BuildCategories>> = buildRepository.getAllBuildStream()

    //ItemDetailFragment
    fun addItemToBuild(item: ItemsDefaultCategories): String {
        var message: String
        try {
            _currentBuild.value!!.buildItems.add(item)
            message = "Item added to build"

        } catch (e: Exception) {
            message = "Failure"
            Log.d(TAG, "$e")
        }
        return message
    }

    private fun getItems() {
        viewModelScope.launch {
            val time = measureTimeMillis {
                try {
                    _listOfWeapons.value =
                        EldenBuildApi.retrofitService.getWeapon(20, page = 3).data

                } catch (e: Exception) {
                    Log.d(NETWORK_TEST, "Failure Weapon  : ${e.message}")
                }
                try {
                    _listOfArmors.value = EldenBuildApi.retrofitService.getArmors().data

                } catch (e: Exception) {
                    Log.d(NETWORK_TEST, "Failure Armor  : ${e.message}")
                }
            }
            Log.d("Teste", "Time = $time")
        }
    }

    fun setItem(typeOfItem: String) {
        when (typeOfItem) {
            Items.ARMOR -> _showItemList.value = listOfArmors.value
            Items.WEAPON -> _showItemList.value = listOfWeapons.value
        }
    }

    init {
        getItems()
    }
}




