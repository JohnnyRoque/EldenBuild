package com.eldenbuild.data

interface ItemsDefaultCategories {
    val id: String
    val name: String
    val image: String
    val description: String
    val category: String get() = ""
    val weight: Double get() = 0.0
}

data class ItemArmors(
    override val id: String,
    override val name: String,
    override val image: String,
    override val description: String,
    override val category: String,
    override val weight: Double,
    val dmgNegation: List<StatsAmount>,
    val resistance: List<StatsAmount>,
    )  :  ItemsDefaultCategories


data class ItemWeapons(
    override val id: String,
    override val name: String,
    override val image: String,
    override val description: String,
    override val category: String,
    override val weight: Double,
    val attack: List<StatsAmount>,
    val defence: List<StatsAmount>,
    val scalesWith: List<StatsScaling>,
    val requiredAttributes: List<StatsAmount>,
) : ItemsDefaultCategories

data class StatsAmount(val name: String, var amount: Int)
data class StatsScaling(val name: String, val scaling: String = "")








