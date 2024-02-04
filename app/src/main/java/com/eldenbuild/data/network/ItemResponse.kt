package com.eldenbuild.data.network

import com.eldenbuild.data.database.ItemsDefaultCategories

data class ItemResponse(
    val success: Boolean,
    val count: Int,
    val total: Int,
    val data: List <ItemsDefaultCategories>
)
