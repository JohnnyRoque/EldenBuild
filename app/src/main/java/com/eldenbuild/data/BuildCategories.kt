package com.eldenbuild.data

import java.util.UUID

data class BuildCategories(
    val title: String,
    val category: String,
    val description: String?,
    val buildItems: MutableList<ItemsDefaultCategories>,
    val buildCharacterStatus: List<CharacterStatus>,
    val buildId: UUID = UUID.randomUUID()
    )

data class CharacterStatus(
    val attributeName: String,
    var attributeLevel: Int,
    var newAttributeLevel: Int,
)

data class ListOfImagesCarousel(val image: Int, val text: String)