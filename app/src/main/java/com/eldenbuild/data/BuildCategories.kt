package com.eldenbuild.data

data class BuildCategories(
    val title:String,
    val category : String,
    val buildItems: MutableList<ItemsDefaultCategories>
)
