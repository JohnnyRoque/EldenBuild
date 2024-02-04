package com.eldenbuild.ui.build_detail_fragment

import androidx.lifecycle.ViewModel
import com.eldenbuild.data.database.BuildCategories
import com.eldenbuild.data.repository.BuildRepository
import kotlinx.coroutines.flow.MutableStateFlow

class BuildDetailViewModel(buildRepository: BuildRepository) : ViewModel() {

    companion object CurrentBuild {
        private val _buildDetail: MutableStateFlow<BuildCategories?> = MutableStateFlow(null)
        val buildDetail: MutableStateFlow<BuildCategories?> = _buildDetail
        private val emptyBuild = BuildCategories(
            title = "",
            category = "",
            description = "",
            buildItems = mutableListOf(),
            buildCharacterStatus = mutableListOf()
        )

        fun getBuildDetail(buildCategories: BuildCategories) = try {
                _buildDetail.value = buildCategories
                buildCategories
            } catch (e: Exception) {
                emptyBuild
            }

    }
}