package com.eldenbuild.data.network
import com.eldenbuild.data.ItemArmors
import com.eldenbuild.data.ItemWeapons

interface ItemResponse {
    val success: Boolean
    val count: Int
    val total: Int
    val data: List<Any>
}

data class ArmorResponse(
    override val success: Boolean,
    override val count: Int,
    override val total: Int,
    override val data: List<ItemArmors>
) : ItemResponse

data class WeaponResponse(
    override val success: Boolean,
    override val count: Int,
    override val total: Int,
    override val data: List<ItemWeapons>
): ItemResponse
