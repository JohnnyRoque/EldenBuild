package com.eldenbuild.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eldenbuild.data.database.BuildCategories
import com.eldenbuild.data.database.CharacterStatus
import com.eldenbuild.data.database.ItemsDefaultCategories
import com.eldenbuild.data.repository.BuildRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

//For i in buildList{ if (buildList.contains(checkList)){deleteBuild) resetCheckList

const val NETWORK_TEST = "network_test"

class OverViewViewModel(
    private val buildRepository: BuildRepository,
) : ViewModel() {

    private val _checkedBuildListUiState: MutableStateFlow<List<BuildCategories>> =
        MutableStateFlow(listOf())

    val checkedBuildListUiState: StateFlow<List<BuildCategories>> = _checkedBuildListUiState

    fun addCheckedBuild(build: BuildCategories) {
        if (!checkedBuildListUiState.value.contains(build)) {
            _checkedBuildListUiState.update { value ->
                mutableListOf<BuildCategories>().also {
                    it.addAll(value)
                    it.add(build)
                }
            }
        }
    }
    fun deleteCheckedBuild(checkedList: List<BuildCategories>): Boolean {
        var isBuildDeleted = false
        try {
            for (i in checkedList) {
                if (buildsList.value.containsAll(checkedList)) {
                    viewModelScope.launch(Dispatchers.IO) {
                        buildRepository.deleteBuild(i)
                        if (!buildsList.value.contains(i)) {
                            isBuildDeleted = true
                        }
                    }
                }
            }

        } catch (e: Exception) {
            Log.d("deleteCheckedList Exception", "$e")
            isBuildDeleted = false
        }
        resetCheckedBuildList()
        return isBuildDeleted
    }

    fun removeCheckedBuild(build: BuildCategories) {
        if (checkedBuildListUiState.value.contains(build)) {
            _checkedBuildListUiState.update { value ->
                mutableListOf<BuildCategories>().also {
                    it.addAll(value)
                    it.remove(build)
                }
            }
        }
    }

    private fun resetCheckedBuildList() {
        _checkedBuildListUiState.update {
            listOf()
        }
    }

    //OverViewViewModel
    val buildsList: StateFlow<List<BuildCategories>> =
        buildRepository.getAllBuildStream()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                initialValue = listOf()
            )

    fun getCurrentBuild(buildId: Int) = buildRepository.getBuildStream(buildId)


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
                Log.d("CreateNewBuild Exception", "$e")
            }
        }
    }
}



