package com.eldenbuild.data.database

interface ItemsDefaultCategories {
    val id: String
    val name: String
    val image: String
    val description: String
    val category: String get() = ""
    val weight: Double get() = 0.0
    val type: String get() = ""
    val cost: Int get() = 0
    val slots: Int get() = 0
    val effects: String get() = ""
    val requires: String get() = ""
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
) : ItemsDefaultCategories


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

data class ItemShields(
    override val id: String,
    override val name: String,
    override val image: String,
    override val description: String,
    override val category: String,
    override val weight: Double,
    val attack: List<StatsAmount>,
    val defence: List<StatsAmount>,
    val requiredAttributes: List<StatsAmount>,
    val scalesWith: List<StatsScaling>
) : ItemsDefaultCategories

data class ItemAshesOfWar(
    override val id: String,
    override val name: String,
    override val image: String,
    override val description: String,
    val affinity: String,
    val skill: String
) : ItemsDefaultCategories
data class ItemIncantations (
    override val id: String,
    override val name: String,
    override val image: String,
    override val description: String,
    override val type: String,
    override val cost: Int,
    override val slots: Int,
    override val effects: String,
    override val requires: String
): ItemsDefaultCategories

data class ItemSorceries (
    override val id: String,
    override val name: String,
    override val image: String,
    override val description: String,
    override val type: String,
    override val cost: Int,
    override val slots: Int,
    override val effects: String,
    override val requires: String
): ItemsDefaultCategories

data class  ItemSpirits(
    override val id: String,
    override val name: String,
    override val image: String,
    override val description: String,
    val fpCost:Int,
    val hpCost: Int,
    override val effects: String
): ItemsDefaultCategories

data class ItemTalismans(
    override val id: String,
    override val name: String,
    override val image: String,
    override val description: String,
    override val effects: String
): ItemsDefaultCategories

data class ItemAmmo(
    override val id: String,
    override val name: String,
    override val image: String,
    override val description: String,
    override val type: String,
    val passive : String,
    val attackPower: List<StatsAmount>
): ItemsDefaultCategories

data class StatsAmount(val name: String, var amount: Int)
data class StatsScaling(val name: String, val scaling: String = "")








