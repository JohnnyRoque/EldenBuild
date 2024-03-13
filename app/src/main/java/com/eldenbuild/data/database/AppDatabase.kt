package com.eldenbuild.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.eldenbuild.util.json_adapter.ListConverter

@Database(entities = [BuildCategories::class], version = 2, exportSchema = false)
@TypeConverters(ListConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun buildsDao(): BuildsDao

}