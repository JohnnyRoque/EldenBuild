package com.eldenbuild.data.database

data class ItemsDefaultCategories(

    val id: String = "",

    val name: String = "",

    val image: String = "",

    val description: String = "",

    val category: String = "",

    val weight: Double = 0.0,

    val type: String = "",

    val cost: Int = 0,

    val slots: Int = 0,

    val effect: String = "",

    val requires: String = "",

    val affinity: String = "",

    val skill: String = "",

    val dmgNegation: List<StatsAmount> = listOf(),

    val resistance: List<StatsAmount> = listOf(),

    val attack: List<StatsAmount> = listOf(),

    val defence: List<StatsAmount> = listOf(),

    val scalesWith: List<StatsScaling> = listOf(),

    val requiredAttributes: List<StatsAmount> = listOf(),

    val fpCost: Int = 0,

    val hpCost: Int = 0,

    val passive: String = "",

    val attackPower: List<StatsAmount> = listOf(),

    var itemType : String = "",

    var fromBuild : String = ""
)

data class StatsAmount(val name: String, var amount: Int?)
data class StatsScaling(val name: String, val scaling: String = "")








