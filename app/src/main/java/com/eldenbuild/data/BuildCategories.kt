package com.eldenbuild.data

import java.util.UUID

data class BuildCategories(
    val title:String,
    val category : String,
    val description : String?,
    val buildItems: MutableList<ItemsDefaultCategories>,
    val buildId : UUID = UUID.randomUUID()
)
