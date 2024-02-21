package com.eldenbuild.ui.build_detail_fragment

import androidx.lifecycle.ViewModel
import com.eldenbuild.data.database.BuildCategories
import com.eldenbuild.data.database.ItemsDefaultCategories
import com.eldenbuild.data.repository.BuildRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class BuildDetailViewModel(buildRepository: BuildRepository) : ViewModel() {

    val checkedItemsUiState: StateFlow<MutableList<ItemsDefaultCategories>> = _checkedItemUiState

    companion object CurrentBuild {

        private val _checkedItemUiState: MutableStateFlow<MutableList<ItemsDefaultCategories>> =
            MutableStateFlow(
                mutableListOf()
            )

        fun resetCheckedItemList() {

            _checkedItemUiState.value.removeAll {
                it.javaClass == ItemsDefaultCategories::class.java

            }
        }

        fun addCheckedItem(item:ItemsDefaultCategories) {
            if (!_checkedItemUiState.value.contains(item)) {
                _checkedItemUiState.value.add(item)
            }        }
        fun removeCheckedItem(item:ItemsDefaultCategories) {
            if (_checkedItemUiState.value.contains(item)) {
                _checkedItemUiState.value.remove(item)
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
            _buildDetail.value = buildCategories
        } catch (e: Exception) {
            emptyBuild
        }
    }
}