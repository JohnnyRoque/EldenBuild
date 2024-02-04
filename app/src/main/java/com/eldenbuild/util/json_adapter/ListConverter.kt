package com.eldenbuild.util.json_adapter

import androidx.room.TypeConverter
import com.eldenbuild.data.database.CharacterStatus
import com.eldenbuild.data.database.ItemsDefaultCategories
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ListConverter {

    @TypeConverter
    fun itemsDefaultCategoriesToJson(mutableList: MutableList<ItemsDefaultCategories>): String {
        return Gson().toJson(mutableList)
    }

    @TypeConverter
    fun jsonToItemsDefaultCategories(string: String): MutableList<ItemsDefaultCategories> {
        return try {
            Gson().fromJson<MutableList<ItemsDefaultCategories>>(string)
        } catch (_: Exception) {
            mutableListOf()
        }
    }

    @TypeConverter
    fun characterStatusToJsonList(mutableList: MutableList<CharacterStatus>): String {
        return Gson().toJson(mutableList)

    }

    @TypeConverter
    fun jsonToCharacterStatusList(string: String): MutableList<CharacterStatus> {
        return try {
            Gson().fromJson<MutableList<CharacterStatus>>(string)
        } catch (_: Exception) {
            mutableListOf()
        }
    }
}

inline fun <reified T> Gson.fromJson(json: String): T =
    fromJson<T>(json, object : TypeToken<T>() {}.type)
