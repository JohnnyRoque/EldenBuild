package com.eldenbuild.util

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.eldenbuild.application.EldenBuildApplication
import com.eldenbuild.viewmodel.OverViewViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            OverViewViewModel(eldenBuildApplication().container.offlineBuildRepository)
        }
    }
}

fun CreationExtras.eldenBuildApplication(): EldenBuildApplication {
    return this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as EldenBuildApplication
}