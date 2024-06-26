package com.eldenbuild.ui.build_detail_fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eldenbuild.data.database.BuildCategories
import com.eldenbuild.data.database.ItemsDefaultCategories
import com.eldenbuild.data.repository.BuildRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BuildDetailViewModel(private val buildRepository: BuildRepository) : ViewModel() {

    val checkedItemUiState: StateFlow<List<ItemsDefaultCategories>> =
        _checkedItemUiState

    fun deleteCheckedItems(checkedItems: List<ItemsDefaultCategories>): String {
        val message: String = try {
            val updatedBuild = checkNotNull(buildDetail.value).copy()
            if (updatedBuild.buildItems.containsAll(checkedItems)) {
                updatedBuild.buildItems.removeAll(checkedItems)
                viewModelScope.launch(Dispatchers.IO) {
                    buildRepository.updateBuild(updatedBuild)
                }
            }
            resetCheckedItemList()
            "Successfully deleted items"

        } catch (e: NullPointerException) {
            "Failed to delete Items from build"
        }
        return message
    }

    fun addCheckedItem(item: ItemsDefaultCategories) {
        if (!checkedItemUiState.value.contains(item)) {
            _checkedItemUiState.update { value ->
                mutableListOf<ItemsDefaultCategories>().also {
                    it.addAll(value)
                    it.add(item)
                }
            }
        }
    }

    fun removeCheckedItem(item: ItemsDefaultCategories) {
        if (_checkedItemUiState.value.contains(item)) {
            _checkedItemUiState.update { value ->
                mutableListOf<ItemsDefaultCategories>().also {
                    it.addAll(value)
                    it.remove(item)
                }
            }
        }
    }

    companion object CurrentBuild {

        private val _checkedItemUiState: MutableStateFlow<List<ItemsDefaultCategories>> =
            MutableStateFlow(
                listOf()
            )
        fun resetCheckedItemList() {
            _checkedItemUiState.update {
                mutableListOf()
            }
        }


        private val _buildDetail: MutableStateFlow<BuildCategories?> = MutableStateFlow(null)
        val buildDetail: StateFlow<BuildCategories?> = _buildDetail

        private val emptyBuild = BuildCategories(
            title = "",
            category = "",
            description = "",
            buildItems = mutableListOf(),
            buildCharacterStatus = mutableListOf()
        )


        fun getBuildDetail(buildCategories: BuildCategories) = try {
            _buildDetail.update { buildCategories }
        } catch (e: Exception) {
            emptyBuild
        }
    }
}