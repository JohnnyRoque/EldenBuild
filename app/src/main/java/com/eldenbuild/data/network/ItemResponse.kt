package com.eldenbuild.data.network
import com.eldenbuild.data.database.ItemArmors
import com.eldenbuild.data.database.ItemWeapons

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

data class ShieldsResponse(
    override val success: Boolean,
    override val count: Int,
    override val total: Int,
    override val data: List<Any>
):ItemResponse

data class AshesOfWarResponse(
    override val success: Boolean,
    override val count: Int,
    override val total: Int,
    override val data: List<Any>

):ItemResponse

data class IncantationsResponse(
    override val success: Boolean,
    override val count: Int,
    override val total: Int,
    override val data: List<Any>
):ItemResponse
data class SorceriesResponse (
    override val success: Boolean,
    override val count: Int,
    override val total: Int,
    override val data: List<Any>
):ItemResponse
data class SpiritsResponse(
    override val success: Boolean,
    override val count: Int,
    override val total: Int,
    override val data: List<Any>
):ItemResponse
data class TalismansResponse(
    override val success: Boolean,
    override val count: Int,
    override val total: Int,
    override val data: List<Any>
):ItemResponse

data class AmmoResponse(
    override val success: Boolean,
    override val count: Int,
    override val total: Int,
    override val data: List<Any>
):ItemResponse