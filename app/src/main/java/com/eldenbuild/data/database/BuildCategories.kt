package com.eldenbuild.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "build_categories")
data class BuildCategories(
    @PrimaryKey(autoGenerate = true) val buildId: Int = 0,
    val title: String,
    val category: String,
    val description: String?,
    @ColumnInfo("build_items") val buildItems: MutableList<ItemsDefaultCategories>,
    @ColumnInfo("build_character_status") val buildCharacterStatus: MutableList<CharacterStatus>,
)

data class CharacterStatus(
    val attributeName: String,
    var attributeLevel: Int,
)

