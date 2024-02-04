package com.eldenbuild.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eldenbuild.data.database.BuildCategories
import com.eldenbuild.data.database.CharacterStatus
import com.eldenbuild.data.database.ItemsDefaultCategories
import com.eldenbuild.data.repository.BuildRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

const val NETWORK_TEST = "network_test"

class OverViewViewModel(
    private val buildRepository: BuildRepository,
) : ViewModel() {

    //OverViewViewModel
    val buildsList: StateFlow<List<BuildCategories>> =
        buildRepository.getAllBuildStream()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                initialValue = mutableListOf()
            )
    fun getCurrentBuild(buildId: Int) = buildRepository.getBuildStream(buildId)


    //BuildDetailFragment  -X-
    private val _currentBuild = MutableLiveData<BuildCategories>()
    val currentBuild: LiveData<BuildCategories> = _currentBuild

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
    //CustomizeBuildFragment -X-


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

    //ItemDetailFragment -X-

//-X-
//    private fun getItems() {
//        viewModelScope.launch {
//            val time = measureTimeMillis {
//                try {
//                    _listOfWeapons.value =
//                        EldenBuildApi.retrofitService.getWeapon(20, page = 3).data
//
//                } catch (e: Exception) {
//                    Log.d(NETWORK_TEST, "Failure Weapon  : ${e.message}")
//                }
//                try {
//                    _listOfArmors.value = EldenBuildApi.retrofitService.getArmors().data
//
//                } catch (e: Exception) {
//                    Log.d(NETWORK_TEST, "Failure Armor  : ${e.message}")
//                }
//            }
//            Log.d("Teste", "Time = $time")
//        }
//    }

}



